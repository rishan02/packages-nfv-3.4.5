from datetime import datetime

import ncs
import ncs.experimental
import ncs.maapi as maapi
import ncs.maagic as maagic

from internal_utils import is_ha_master_or_no_ha
from internal_utils import is_notif_handling_required

"""
   This module handles the notifications received over Netconf from the ESC.
   It will collect and act upon the notifications and re-deploy any services
   that are dependent on these notifications.
"""


class NetconfNotifSub(ncs.experimental.OperSubscriber):
    def init(self):
        self.register('/ncs:devices/ncs:device/ncs:netconf-notifications/'
                      'ncs:received-notifications')

    def pre_iterate(self):
        return []

    def should_iterate(self):
        if is_ha_master_or_no_ha() and is_notif_handling_required():
            self.log.debug("HA disabled or HA role is MASTER")
            return True
        else:
            self.log.debug(("HA enabled and HA role is not MASTER,"
                            " skipping notifications from the VNFM"))
            return False

    # determine if post_iterate() should run
    def should_post_iterate(self, state):
        return state != []

    def iterate(self, kp, op, oldv, newv, state):
        if op == ncs.MOP_CREATED:
            esc = kp[4][0]
            state.append(_NotifInfo(str(esc), str(kp)))
        return ncs.ITER_CONTINUE

    def post_iterate(self, state):

        context = "system"

        with maapi.single_read_trans("", context) as ro_th:
            ro_root = maagic.get_root(ro_th)
            for notifInfo in state:
                notif = maagic.get_node(ro_th, notifInfo.kp)
                if not notif.data.escEvent:
                    continue
                event = notif.data.escEvent
                username = self._get_username(ro_root, notifInfo.esc, event)
                if username and notif.subscription == "tailf_nfvo":
                    # Switch user context
                    with maapi.single_write_trans(username, context) as th:
                        self._vnf_info_svcs_to_redeploy = set()
                        self._handle_notif(notifInfo.esc, notif, th)
                        th.apply()

                    if self._vnf_info_svcs_to_redeploy:
                        with maapi.single_read_trans(username, context) as th:
                            self._redeploy_services(th)

    def _get_username(self, ro_root, esc_name, event):
        vdl = ro_root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment
        key = [event.tenant, event.depname, esc_name]
        if key in vdl:
            return vdl[key].username

        return None

    def _handle_notif(self, esc_name, notif, th):
        self.log.info("Handling event from {}".format(esc_name))
        event = notif.data.escEvent
        event_type = event.event.type
        self.log.debug("Handling event type {}".format(event_type))
        if "VM_ALIVE" == event_type:
            self._vm_alive(esc_name, event, th)
        elif "VM_DEPLOYED" == event_type or "VM_RECOVERY_DEPLOYED" == event_type:
            self._vm_deployed(esc_name, event, th)
        elif "SERVICE_ALIVE" == event_type:
            self._service_alive(esc_name, event, th)
        elif "VM_RECOVERY_INIT" == event_type:
            self._recovery_init(esc_name, event, th)
        elif "VM_RECOVERY_COMPLETE" == event_type:
            self._vm_recovery_complete(esc_name, event, th)
        elif "VM_RECOVERY_CANCELLED" == event_type:
            self._recovery_cancelled(esc_name, event, th)
        elif "VM_UNDEPLOYED" == event_type:
            self._vm_undeployed(esc_name, event, th)
        elif "VM_RECOVERY_REBOOT" == event_type:
            self.log.info("VM_RECOVERY_REBOOT - nothing to do")
        elif "SERVICE_UNDEPLOYED" == event_type:
            self.log.info("TODO %s" % (event_type))
        elif "VM_RECOVERY_UNDEPLOYED" == event_type:
            self._vm_recovery_undeployed(esc_name, event, th)
        elif "VM_SCALE_OUT_INIT" == event_type:
            self.log.info("Scale out init")
            pass
        elif "VM_SCALE_OUT_DEPLOYED" == event_type:
            self.log.info("Scale out VM deployed")
            pass
        elif "VM_SCALE_OUT_COMPLETE" == event_type:
            self.log.info("Scale out complete")
            pass
        elif "VM_SCALE_IN_INIT" == event_type:
            self.log.info("Scale in init")
            pass
        elif "VM_SCALE_IN_COMPLETE" == event_type:
            self.log.info("Scale in complete")
            pass
        else:
            self.log.error("Can't handle %s" % (event_type))

        key = [str(event.tenant), str(event.depname), esc_name]
        try:
            # Is it enough to put the plan in error-state? Recovery-state?
            # Just do redeploy and let the plan subscriber figure out what to
            # do?
            #
            root = maagic.get_root(th)
            if key in root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment:
                dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment[key]
                self._vnf_info_svcs_to_redeploy.add(dep._path)
        except KeyError:
            # Handles race condition when the service has been removed
            self.log.info("Could not redeploy %s.  (Has it been removed)?" %
                          (key))

    def _vm_undeployed(self, esc_name, event, th):
        (tenant, depname, vm_group) = self._extract_vm_info(event)

        root = maagic.get_root(th)
        esc = root.ncs__devices.device[esc_name]
        dep_res = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                                          depname,
                                                                          esc_name]
        (vdu, device) = find_vdu_and_device_by_vmid(dep_res.vdu, event.vm_source.vmid)
        self.log.debug("VM_UNDEPLOYED: VDU %s DEV %s" % (vdu,device))
        if device is not None:
            devName = device.device_name
            self.log.debug("GOT VM_UNDEPLOYED: for Device %s" % devName)
            if "FAILURE" == event.status:
                self.log.error("Received failed undeploy: %s" % (event.status_message))
                self._set_device_status_error(th, esc_name, event, vdu.vnf_info,
                                              vdu.vdu, event.status_message)
                return
            del root.ncs__devices.device[devName]
            self._remove_device_lookup(esc_name, event, th, vdu.vnf_info, vdu.vdu)
        else:
            self.log.debug("GOT VM_UNDEPLOYED: for unknown device %s"
                           % str(event.vm_source.vmid))


    def _recovery_init(self, esc_name, event, th):
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)

        esc = root.ncs__devices.device[esc_name]
        esc_config = self._esc_config(esc, tenant, depname, vm_group)
        vm_properties = esc_config.extensions.extension['NSO'].properties.property
        vnf_info = vm_properties['VNF-INFO-NAME'].value
        vdu = vm_properties['VDU'].value
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                                      depname,
                                                                      esc_name]
        device_list = dep.vdu[vnf_info, vdu].vm_device
        dev = device_list[esc_name, event.vm_source.vmid]
        dev.status.recovering = datetime.utcnow().isoformat()
        dep.status.recovering = datetime.utcnow().isoformat()

    def _vm_alive(self, esc_name, event, th):
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment[[tenant,
                                                                depname,
                                                                esc_name]]
        esc = root.ncs__devices.device[esc_name]
        esc_config = self._esc_config(esc, tenant, depname, vm_group)
        vm_properties = esc_config.extensions.extension['NSO'].properties.property
        vnf_info = vm_properties['VNF-INFO-NAME'].value
        vdu = vm_properties['VDU'].value

        if not self._is_vdu_managed(dep, vnf_info, vdu):
            self._set_device_status_alive(th, esc_name, event, vnf_info, vdu)
            self.log.debug('Skipping non managed vdu %s' % (vdu))
            return

        device_name = self._get_device_name_from_lookup(th, esc_name, event, vnf_info, vdu)

        vm_device = root.ncs__devices.device[device_name]

        try:
            device_type = vm_properties['NEDTYPE'].value
            protocol = None
            if 'NEDPROTO' in vm_properties:
                protocol = vm_properties['NEDPROTO'].value

            if ('cli' == device_type and 'ssh' == protocol) or \
                    'netconf' == device_type:
                self.log.info("Fetching ssh keys from %s" % (device_name))
                output = vm_device.ssh.fetch_host_keys()
                if 'failed' == output.result:
                    raise Exception("Failed fetching ssh keys from %s with \
                                    status %s due to: %s" % (device_name, output.result, output.info))
            elif 'generic' == device_type:
                    self.log.info("Trying to fetch ssh keys from generic \
                            device %s" % (device_name))
                    output = vm_device.ssh.fetch_host_keys()
                    if 'failed' == output.result:
                        self.log.error("Failed fetching ssh keys from %s with \
                                status %s due to: %s" % (device_name, output.result, output.info))

            if event.event.type == "VM_RECOVERY_COMPLETE":
                sync_direction = dep.nfvo_behaviors.device_recovery_sync_direction
                if sync_direction == "from":
                    self.log.info("Syncing from %s" % (device_name))
                    output = vm_device.sync_from()
                    if not output.result:
                        raise Exception("Failed syncing from %s with status %s due to: %s" % (
                            device_name, output.result, output.info))
                elif sync_direction == "to":
                    self.log.info("Syncing to %s" % (device_name))
                    output = vm_device.sync_to()
                    if not output.result:
                        raise Exception("Failed syncing to %s with status %s due to: %s" % (
                            device_name, output.result, output.info))
                elif sync_direction == "none":
                    self.log.info("No Syncing for %s" % (device_name))
            else:
                self.log.info("Syncing from %s" % (device_name))
                output = vm_device.sync_from()
                if not output.result:
                    raise Exception("Failed syncing from %s with status %s due to: %s" % (
                        device_name, output.result, output.info))
        except Exception as e:
            self._set_device_status_error(th, esc_name, event, vnf_info,
                                          vdu, str(e))
            return
        self._set_device_status_alive(th, esc_name, event, vnf_info, vdu)

    def _vm_deployed(self, esc_name, event, th):
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment[[tenant, depname, esc_name]]

        esc = root.ncs__devices.device[esc_name]

        # If it is a VM_DEPLOYED event we do not get a vm_group
        # so we cannot find the esc_config
        if ("VM_DEPLOYED" == event.event.type) and ("FAILURE" == event.status):
            self.log.error("Received failed deploy: {}".format(event.status_message))
            return

        esc_config = self._esc_config(esc, tenant, depname, vm_group)
        vm_properties = esc_config.extensions.extension['NSO'].properties.property
        vnf_info = vm_properties['VNF-INFO-NAME'].value
        vdu = vm_properties['VDU'].value

        event_type = event.event.type
        if "VM_DEPLOYED" == event_type:
            device_name = self._add_device_lookup(th, esc_name,
                                                  event, dep, vnf_info, vdu)
        else:
            # For VM_RECOVERY_DEPLOYED, we should have the device already
            device_name = self._copy_device_info(th, esc_name, event,
                                                 vnf_info, vdu)

        self.log.info("event.status = {}".format(event.status))
        if "FAILURE" == event.status:
            self.log.error("Received failed deploy: %s" % (event.status_message))
            self._set_device_status_error(th, esc_name, event, vnf_info,
                                          vdu, event.status_message)
            return

        if not self._is_vdu_managed(dep, vnf_info, vdu):
            self.log.debug('Skipping non managed vdu %s' % (vdu))
            return

        # We have a managed device, let's add it to the device tree
        # TODO: Will this work if the device is already is created
        vm_device = root.ncs__devices.device.create(device_name)

        # Netsim?
        if event.event.details:
            mgmt_ip = "127.0.0.1"
            vm_device.port = self._get_netsim_port(event)
        else:
            mgmt_nic_id = vm_properties['MGMTIF'].value
            if "VM_DEPLOYED" == event_type:
                mgmt_ip = event.vm_source.interfaces.interface[mgmt_nic_id].ip_address
            else:
                mgmt_ip = event.vm_target.interfaces.interface[mgmt_nic_id].ip_address

        self.log.info('mgmt_ip: %s' % (mgmt_ip))
        vm_device.address = mgmt_ip
        vm_device.authgroup = vm_properties['AUTHGRP'].value

        if 'DEVICE-PROFILE' in vm_properties:
            vm_device.device_profile = vm_properties['DEVICE-PROFILE'].value

        # ADD the host-key-verification
        host_key_ver_strategy = vm_properties['HOSTKEY-VERIFICATION-TYPE'].value
        self.log.debug("Setting host key verification to {}".format(host_key_ver_strategy))
        vm_device.ssh.host_key_verification = host_key_ver_strategy
        device_type = vm_properties['NEDTYPE'].value

        if 'cli' == device_type:
            dt = vm_device.device_type.cli.create()
            dt.ned_id = vm_properties['NEDID'].value
            if 'NEDPROTO' in vm_properties:
                dt.protocol = vm_properties['NEDPROTO'].value
            if 'MGMT-PORT' in vm_properties:
                vm_device.port = vm_properties['MGMT-PORT'].value
        elif 'generic' == device_type:
            dt = vm_device.device_type.generic.create()
            dt.ned_id = vm_properties['NEDID'].value
        elif 'netconf' == device_type:
            vm_device.device_type.netconf.create()

        vm_device.state.admin_state = 'unlocked'
        # Adding of the service to the list of services to be redeployed is
        # handled in handle_notif

    def _vm_recovery_complete(self, esc_name, event, th):
        # We assume the management IP hasn't changed here
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)

        esc = root.ncs__devices.device[esc_name]
        esc_config = self._esc_config(esc, tenant, depname, vm_group)
        vm_properties = esc_config.extensions.extension['NSO'].properties.property
        vnf_info = vm_properties['VNF-INFO-NAME'].value
        vdu = vm_properties['VDU'].value
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                                      depname,
                                                                      esc_name]
        if "FAILURE" == event.status:
            error_message = event.status_message
            self.log.error("Received failed recovery: %s" %
                           (error_message))
            self._set_device_status_error(th, esc_name, event, vnf_info,
                                          vdu, error_message)

            if dep.status.ready:
                del dep.status.ready

            dep.status.error = error_message
            return
        elif event.vm_source.vmid == event.vm_target.vmid:
            # Update vm device
            self.log.debug("VM_RECOVERY_COMPLETE with identical source and"
                           " target vmid:s. Assuming succesful reboot.")
            self._vm_alive(esc_name, event, th)
        else:
            # The vm_source contains the old vmid, remove that
            self._remove_device_lookup(esc_name, event, th, vnf_info, vdu)
            # Setup the new device
            self._vm_alive(esc_name, event, th)

        # Are all device ok now? Then set deployment-result to ready!
        num_devices = len([1 for vdu in dep.vdu for vm_device in vdu.vm_device])
        self.log.debug("Check {} device(s) for readiness".format(num_devices))
        for vdu in dep.vdu:
            for vm_device in vdu.vm_device:
                if vm_device.status.ready:
                    continue
                elif vm_device.vmid == event.vm_target.vmid:
                    self.log.debug("Target device set to ready, skipping..")
                    continue
                else:
                    self.log.debug("{} is NOT ready".format(vm_device.device_name))
                    return
        self.log.info("All devices in ready state, setting deployment ready")
        now = datetime.utcnow()
        dep.status.ready = now.isoformat()

    def _recovery_cancelled(self, esc_name, event, th):
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)

        esc = root.ncs__devices.device[esc_name]
        esc_config = self._esc_config(esc, tenant, depname, vm_group)
        vm_properties = esc_config.extensions.extension['NSO'].properties.property
        vnf_info = vm_properties['VNF-INFO-NAME'].value
        vdu = vm_properties['VDU'].value
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                             depname,
                                                             esc_name]
        device_list = dep.vdu[vnf_info, vdu].vm_device
        dev = device_list[esc_name, event.vm_source.vmid]
        dev.status.ready = datetime.utcnow().isoformat()
        dep.status.ready = datetime.utcnow().isoformat()

    def _vm_recovery_undeployed(self, esc_name, event, th):
        # Should we remove device_lookup here?
        # We assume the management IP hasn't changed here
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)

        esc = root.ncs__devices.device[esc_name]
        esc_config = self._esc_config(esc, tenant, depname, vm_group)
        vm_properties = esc_config.extensions.extension['NSO'].properties.property
        vnf_info = vm_properties['VNF-INFO-NAME'].value
        vdu = vm_properties['VDU'].value
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                             depname,
                                                             esc_name]

        self.log.debug("Recovery undeployed for {} {} {}".format(tenant, depname, vm_group))

        device_list = dep.vdu[vnf_info, vdu].vm_device
        dev = device_list[esc_name, event.vm_source.vmid]
        dev.status.recovering = datetime.utcnow().isoformat()
        dep.status.recovering = datetime.utcnow().isoformat()
        self.log.info("Setting device {} undeployed".format(dev))


    def _service_alive(self, esc_name, event, th):
        if "FAILURE" == event.status:
            self.log.error("Received failed service alive: %s" %
                          (event.status_message))
            self._set_vnf_deployment_ready(th, esc_name, event,
                                           False, event.status_message)
            return
        else:
            # What happens when we have more than once ESC device?
            self._set_vnf_deployment_ready(th, esc_name, event, True)

        # Adding of the service to the list of services to be redeployed is
        # handled in handle_notif

    def _redeploy_services(self, th):
        for svc in self._vnf_info_svcs_to_redeploy:
            self.log.info("Redeploying '%s'" % (svc))
            redep = maagic.get_node(th, "%s/nfvo-rel2-esc:reactive-re-deploy" % (svc))
            redep()

    def _set_vnf_deployment_ready(self, th, esc_name, event, is_ready, error_message=''):
        root = maagic.get_root(th)
        self.log.debug("event {}".format(event))
        key = [str(event.tenant), str(event.depname), esc_name]
        if key in root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result:
            dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[key]
            if is_ready:
                now = datetime.utcnow()
                dep.status.ready = now.isoformat()
            elif not is_ready and error_message == '':
                dep.status.deployed.create()
            else:
                self.log.debug("got {}".format(error_message))
                dep.status.error = error_message
        else:
            (tenant, depname, vm_group) = self._extract_vm_info(event)
            root = maagic.get_root(th)
            dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result.create(tenant,
                                                                        depname,
                                                                        esc_name)

            if event.status == "FAILURE" and error_message is not None:
                # IF we do not have a vnf_deployment_result, this event is a
                # SERVICE_ALIVE failure on an deployment initial configuration.
                # So we should setup the deployment_result at least.
                dep.status.error = error_message
            else:
                dep.status.error = "Unknown Error: Missing status message in escEvent"


    def _add_device_lookup(self, th, esc_name, event, dep, vnf_info, vdu):
        (tenant, depname, vm_group) = self._extract_vm_info(event)
        vmid = event.vm_source.vmid
        root = maagic.get_root(th)
        res = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result.create(tenant, depname, esc_name)
        device_list = res.vdu.create(vnf_info, vdu).vm_device

        # Convert conf trans into oper transaction
        device_list = res.vdu.create(vnf_info, vdu).vm_device
        key = [esc_name, vmid]
        if key in device_list:
            return device_list[key].device_name

        index = 0
        for dev in device_list:
            if dev.esc_device != esc_name:
                continue
            index = max(index, dev.index)
        device_index = index + 1
        device_name = ""
        try:
            # Get the device-name-pool and pick the device-name with device_index
            # from that pool
            dnpool = dep.vnf_info[vnf_info].vdu[vdu].device_name_pool.device_name
            if dnpool is not None and len(dnpool) > 0:
                self.log.debug("Getting device_name from pool...")
                device_name = dnpool[index]
                self.log.debug("Using device name from device_name_pool {}".format(device_name))
            else:
                device_name = "%s-%s-%s-%s-%d" % (tenant,
                                                  depname,
                                                  vm_group,
                                                  esc_name,
                                                  device_index)
        except IndexError:
            self.log.error("ERROR: Could not get hostname, setting default...")
            self.log.debug("Error getting hostname from device_pool"
                           " using default")
            device_name = "%s-%s-%s-%s-%d" % (tenant,
                                                 depname,
                                                 vm_group,
                                                 esc_name,
                                                 device_index)


        # Pick device name from list
        dev = device_list.create(esc_name, vmid)
        dev.index = device_index
        dev.device_name = device_name
        dev.status.deployed.create()

        ### I want to add the compute node hostname to the deployment result, this is how it would look for vm_deployed:

        # run show nfvo vnfr esc vnf-deployment-result
        #         DEPLOYMENT                                          ESC
        # TENANT  NAME        DEPLOYED  READY  ERROR  VNFR       VDU  DEVICE  VMID                                  INDEX  DEVICE NAME                       HOSTID  HOSTNAME  DEPLOYED  READY  ERROR  RECOVERING
        # ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        # admin   ntt68       -         -      -      CSR1kvNTT  CSR  esc0    10c2a52a-9f03-4909-ba70-730e17f6d574  2      admin_ntt68_CSR1kvNTT_CSR_esc0_2  -       oc2       X         -      -      -
        #                                                             esc0    b05f5ac7-403c-478e-a17d-1c7bb3117616  1      admin_ntt68_CSR1kvNTT_CSR_esc0_1  -       oc2       X         -      -      -

        if event.vm_source.hostname is not None:
            self.log.debug("Setting compute hostname to {}".format(event.vm_source.hostname))
            dev.hostname = event.vm_source.hostname
        else:
            self.log.debug("No compute hostname in escEvent {}".format(event))

        return device_name

    def _copy_device_info(self, th, esc_name, event, vnf_info, vdu):
        root = maagic.get_root(th)
        source_vmid = event.vm_source.vmid
        dest_vmid = event.vm_target.vmid
        (tenant, depname, _) = self._extract_vm_info(event)
        res = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant, depname, esc_name]
        device_list = res.vdu[vnf_info, vdu].vm_device

        old_dev = device_list[esc_name, source_vmid]
        dev = device_list.create(esc_name, dest_vmid)
        dev.index = old_dev.index
        dev.device_name = old_dev.device_name

        # if not dev.status.deployed.exists():
        if source_vmid != dest_vmid:
            # This could happen if the source and dest vmid is equal
            dev.status.deployed.create()
        return str(dev.device_name)

    def _remove_device_lookup(self, esc_name, event, th, vnf_info, vdu):
        # Try to remove the device from the lookup table
        root = maagic.get_root(th)
        (tenant, depname, vm_group) = self._extract_vm_info(event)
        depKey = [tenant, depname, esc_name]
        if depKey not in root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result:
            return
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[depKey]
        del dep.vdu[vnf_info, vdu].vm_device[esc_name, event.vm_source.vmid]
        if not dep.vdu[vnf_info, vdu].vm_device:
            del dep.vdu[vnf_info, vdu]

    def _get_device_name_from_lookup(self, th, esc_name, event, vnf_info, vdu):
        (tenant, depname, _) = self._extract_vm_info(event)
        if "VM_ALIVE" == event.event.type:
            vmid = event.vm_source.vmid
        else:
            #VM_RECOVERY_DEPLOYED or VM_RECOVERY_COMPLETE
            vmid = event.vm_target.vmid

        root = maagic.get_root(th)
        res = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant, depname,
                                                             esc_name]
        device_list = res.vdu[vnf_info, vdu].vm_device
        key = [esc_name, vmid]
        return str(device_list[key].device_name)

    def _set_device_status_alive(self, th, esc_name, event, vnf_info, vdu):
        (tenant, depname, _) = self._extract_vm_info(event)
        if "VM_ALIVE" == event.event.type:
            vmid = event.vm_source.vmid
            hostid = event.vm_source.hostid
            hostname = event.vm_source.hostname
        elif "VM_RECOVERY_COMPLETE" == event.event.type:
            vmid = event.vm_target.vmid
            hostid = event.vm_target.hostid
            hostname = event.vm_target.hostname

        root = maagic.get_root(th)
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                             depname,
                                                             esc_name]
        device_list = dep.vdu[vnf_info, vdu].vm_device
        key = [esc_name, vmid]
        dev = device_list[key]
        dev.hostid = hostid
        if hostname is not None:
            self.log.debug("Setting hostname to {}".format(hostname))
            dev.hostname = hostname
        else:
            self.log.debug("No compute hostname in escEvent {}".format(event))
        dev.status.ready = datetime.utcnow().isoformat()

    def _set_device_ios_hostname(self, th, esc_name, event, vnf_info, vdu, ios_hostname):
        (tenant, depname, _) = self._extract_vm_info(event)
        if "VM_ALIVE" == event.event.type:
            vmid = event.vm_source.vmid
            hostid = event.vm_source.hostid
            hostname = event.vm_source.hostname
        elif "VM_RECOVERY_COMPLETE" == event.event.type:
            vmid = event.vm_target.vmid
            hostid = event.vm_target.hostid
            hostname = event.vm_target.hostname

        root = maagic.get_root(th)
        res = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant, depname]
        device_list = res.vdu[vnf_info, vdu].vm_device
        key = [esc_name, vmid]
        dev = device_list[key]
        if ios_hostname is not None:
            self.log.debug("Setting hostname to {}".format(hostname))
            dev.device_hostname = ios_hostname
        else:
            self.log.debug("No IOS hostname in config")

    def _set_device_status_error(self, th, esc_name, event, vnf_info,
                                 vdu, error_msg):
        (tenant, depname, _) = self._extract_vm_info(event)
        vmid = event.vm_source.vmid
        self.log.debug("Device {} recovery error '{}'".format(vmid, error_msg))
        root = maagic.get_root(th)
        dep = root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                                      depname,
                                                                      esc_name]
        device_list = dep.vdu[vnf_info, vdu].vm_device
        key = [esc_name, vmid]
        dev = device_list[key]
        dev.status.error = error_msg

    def _extract_vm_info(self, event):
        return (str(event.tenant),
                str(event.depname),
                str(event.vm_group))

    def _esc_config(self, esc, tenant, depname, vm_group):
        self.log.info("_esc_config esc: %s, tenant: %s, depname: %s, vm_group: %s" %
                      (esc, tenant, depname, vm_group))
        return esc.config.esc_datamodel.tenants.tenant[tenant]\
            .deployments.deployment[depname].vm_group[vm_group]

    def _is_vdu_managed(self, dep, vnf_info, vdu):
        return dep.vnf_info[vnf_info].vdu[vdu].managed

    def _get_netsim_port(self, event):
        tokens = str(event.event.details).split(':')
        return tokens[0]

def find_vdu_and_device_by_vmid(vdu, vmid):
    for d in vdu:
        for vmd in d.vm_device:
            if vmd.vmid == vmid:
                return (d, vmd)
    return (None, None)

class _NotifInfo:
    def __init__(self, esc, kp):
        self.esc = esc
        self.kp = kp
