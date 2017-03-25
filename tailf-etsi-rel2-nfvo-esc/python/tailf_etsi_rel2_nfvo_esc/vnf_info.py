import ncs
from ncs.application import Service, PlanComponent
import internal_utils


class VNFBackend(Service):
    """
    Implements the service point 'tailf-etsi-rel2-nfvo-esc-vnf-info', which is called when
    config is written to /nfvo-rel2:nfvo/tailf-esc-internal/vnf-deployment .
    This config is written to by the vnf_subscriber
    """
    @Service.create
    def cb_create(self, tctx, root, service, proplist):
        self.tctx = tctx
        self.root = root
        self.service = service
        self.proplist = proplist
        self.template = ncs.template.Template(self.service)

        if 'not-instantiated' == service.state:
            self.log.info("VNF info will not be instantiated")
            return

        self.log.info('Deployment create')
        self.log.debug("ESC {}".format(service.esc))
        self.esc = service.esc

        for vnf_info in service.vnf_info:
            self.log.info('VNF %s' % (vnf_info.name))
            vnfd = root.nfvo_rel2__nfvo.vnfd[vnf_info.vnfd]
            self._handle_vnfd(vnfd, vnf_info)

        for nw in service.additional_network:
            self.log.info('Additional network: %s' % (nw.network_name))
            self._create_nw(nw)

    def _handle_vnfd(self, vnfd, vnf_info):
        self.vnfd = vnfd
        self.vnf_info = vnf_info

        for vdu in vnfd_flavour_vdus(self.vnf_info, self.vnfd):
            self._apply_per_vdu_instance(vdu)

    def _apply_per_vdu_instance(self, vdu):
        vm_group_vars = ncs.template.Variables()

        vm_group_vars.add('VM_GROUP_NAME', vm_group_name(self.vnf_info, vdu))
        vm_group_vars.add('VNF_INFO_NAME', self.vnf_info.name)
        vm_group_vars.add('VNF', self.vnf_info.vnfd)
        vm_group_vars.add('VNFD', self.vnfd.id)
        vm_group_vars.add('VDU', vdu.id)
        (min_active, max_active) = _get_min_max_number_of_instances(self.vnfd, self.vnf_info, vdu)
        vm_group_vars.add('MIN_ACTIVE', min_active)
        vm_group_vars.add('MAX_ACTIVE', max_active)

        self.log.info(vm_group_vars)
        self.template.apply('tailf-etsi-rel2-nfvo-esc-vm-group', vm_group_vars)
        self._apply_interfaces(vdu)
        self._apply_day0(vdu)

        self._apply_vdu_order(vdu)

        self._apply_device_templates(vdu)

    def _apply_interfaces(self, vdu):
        for cp in vdu.internal_connection_point_descriptor:
            vars = ncs.template.Variables()
            vars.add('VNF', self.vnf_info.vnfd)
            vars.add('VDU', vdu.id)
            vars.add('VM_GROUP_NAME', vm_group_name(self.vnf_info, vdu))
            vars.add('NICID', cp.interface_id)

            if cp.type is None:
                vars.add('TYPE', "")
            else:
                vars.add('TYPE', cp.type)

            if cp.virtual_link_descriptor:
                # Need to create internal network
                network_name = self._create_internal_network(cp.virtual_link_descriptor)
                vars.add('NETWORK', network_name)
            else:
                # If we don't have an internal_vlr for this cp,
                # the VNFD has to define an external connection point
                ext_cp_name = cp.external_connection_point_descriptor
                self.log.info("Internal CP: %s <-> External CP: %s" %
                              (cp.id, ext_cp_name))
                cpl = self.vnf_info.vnfd_connection_point
                if ext_cp_name not in cpl:
                    raise Exception('No external VLD specified for %s:%s '
                                    '<-> %s:%s'
                                    % (vdu.id, cp.id,
                                       self.vnfd.id, ext_cp_name))

                if self.vnfd.external_connection_point_descriptor[ext_cp_name].management:
                    self.log.info("'%s' is the management interface" %
                                  (ext_cp_name))

                    vars.add('NEDTYPE', '')
                    vars.add('NEDPROTO', '')
                    vars.add('NEDID', '')
                    vars.add('AUTHGRP', '')
                    vars.add('MGMT_PORT', '')
                    vars.add('HOSTKEY_VERIFICATION_TYPE', '')
                    if self.vnf_info.vdu[vdu.id].managed:
                        vars = self._set_ned_variables(vdu, vars)

                    self.log.info('vm-esc-mgmt-interface vars ', vars)

                    vars.add('VNF_INFO_NAME', self.vnf_info.name)
                    self.template.apply('tailf-etsi-rel2-nfvo-esc-mgmt-interface',
                                        vars)

                vnfd_cp = cpl[ext_cp_name]
                network_name = vnfd_cp.network_name
                vars.add('NETWORK', network_name)

            internal_cp_list = self.vnf_info.vdu[vdu.id].internal_connection_point

            vars.add('IP_ADDRESS', '')
            vars.add('IP_START', '')
            vars.add('IP_END', '')
            vars.add('IP_GATEWAY', '')
            vars.add('IP_NETMASK', '')
            vars.add('MAC_ADDR', '')
            if cp.id in internal_cp_list:
                icp = internal_cp_list[cp.id]
                if icp.connection_point_address:
                    addrs = icp.connection_point_address
                    self.log.info("Adding IP for for ", cp.id)
                    if addrs.address:
                        vars.add('IP_ADDRESS', addrs.address)

                    if addrs.netmask:
                        vars.add('IP_NETMASK', addrs.netmask)

                    if addrs.gateway:
                        vars.add('IP_GATEWAY', addrs.gateway)

                    if addrs.mac_address:
                        vars.add('MAC_ADDR', addrs.mac_address)

                    vars.add('IP_START', addrs.start)
                    vars.add('IP_END', addrs.end)
                else:
                    self.log.info("No IP assignment for ", cp.id)

                for addr_pair in icp.allowed_address_pair:
                    vars.add('PAIRIPADDR', addr_pair.address)
                    vars.add('PAIRNETMASK', addr_pair.netmask)
                    self.template.apply('tailf-etsi-rel2-nfvo-esc-vm-if-allowed-addr-pairs', vars)

            self.template.apply('tailf-etsi-rel2-nfvo-esc-vm-interface', vars)

    def _set_ned_variables(self, vdu, vars):
        if vdu.device_type.port:
            vars.add('MGMT_PORT', vdu.device_type.port)

        if vdu.device_type.cli.ned_id:
            vars.add('NEDID', vdu.device_type.cli.ned_id)
            vars.add('NEDTYPE', 'cli')
            vars.add('NEDPROTO', vdu.device_type.cli.protocol)
        elif vdu.device_type.netconf:
            vars.add('NEDTYPE', 'netconf')
        elif vdu.device_type.generic:
            vars.add('NEDID', vdu.device_type.generic.ned_id)
            vars.add('NEDTYPE', 'generic')

        vars.add('AUTHGRP', "")
        if self.vnf_info.vdu[vdu.id].authgroup:
            vars.add('AUTHGRP', self.vnf_info.vdu[vdu.id].authgroup)

        if self.vnf_info.vdu[vdu.id].host_key_verification_type:
            self.log.debug("Setting host key verification type to {}".format(self.vnf_info.vdu[vdu.id].host_key_verification_type))
            vars.add('HOSTKEY_VERIFICATION_TYPE', self.vnf_info.vdu[vdu.id].host_key_verification_type)

        return vars

    def _create_internal_network(self, internal_vlr_name):
        self.log.info("_create_internal_network: %s" % internal_vlr_name)
        vlrs = self.vnf_info.virtual_link
        if internal_vlr_name not in vlrs:
            raise Exception("No VLD network data found for %s" %
                            (internal_vlr_name))

        internal_vlr = vlrs[internal_vlr_name]
        network_name = self.vnf_info.name + "-" + internal_vlr_name
        if internal_vlr.subnet:
            self._create_network(network_name, internal_vlr)

        return network_name

    def _apply_day0(self, vdu):
        if vdu.id in self.vnf_info.vdu:
            day0s = self.vnf_info.vdu[vdu.id].day0
            for day0 in day0s:
                self.log.info("day0 %s" % day0.destination)
                vars = ncs.template.Variables()
                vars.add('VDU', vdu.id)
                vars.add('DEST', day0.destination)
                if day0.url:
                    vars.add('FILE', day0.url)
                    vars.add('DATA', "")
                if day0.data:
                    vars.add('FILE', "")
                    vars.add('DATA', day0.data)

                vars.add('VM_GROUP_NAME', vm_group_name(self.vnf_info, vdu))
                self.template.apply('tailf-etsi-rel2-nfvo-esc-day0', vars)

                leaflistvars = list()

                for var in day0.variable:
                    self.log.debug("day0 variable {}".format(var.name))
                    #                    if isinstance(var.value, list) and len(var.value) > 1:
                    if var.value and len(var.value) > 1:
                        self.log.debug("Collecting day0 variable {} for manual write".format(var.name))
                        leaflistvars.append((str(var.name), var.value))
                    else:
                        self.log.debug("Collecting day0 variable {} for template write".format(var.name))
                        vars.add('VAR_NAME', var.name)
                        # var.value is now a leaf-list
                        if var.value:
                            vars.add('VAR_VALUE', var.value[0])
                        else:
                            vars.add('VAR_VALUE', '')

                        self.template.apply('tailf-etsi-rel2-nfvo-esc-day0-var', vars)

                esc_cfg = self.root.ncs__devices.device[self.esc].config
                esc_dm = esc_cfg.esc_datamodel

                tenant = self.service.tenant
                dep_name = self.service.deployment_name
                dep = esc_dm.tenants.tenant[tenant].deployments.deployment[dep_name]
                day0_conf_data = dep.vm_group[vm_group_name(self.vnf_info, vdu)].config_data.configuration[day0.destination]
                for ll_var in leaflistvars:
                    self.log.debug("Configuring day0 variable {} with multiple values".format(ll_var[0]))
                    v = day0_conf_data.variable.create(ll_var[0])
                    v.val = ll_var[1]

    def _apply_vdu_order(self, vdu):
        vars = ncs.template.Variables()
        vars.add('VM_GROUP_NAME', vm_group_name(self.vnf_info, vdu))
        for dep in vdu.depends_on:
            d_vdu = self.vnfd.vdu[dep.vdu]
            vars.add('DEST_VM_GROUP_NAME', vm_group_name(self.vnf_info, d_vdu))
            self.log.info("%s depends on %s" % (vdu.id, d_vdu.id))
            self.template.apply('tailf-etsi-rel2-nfvo-esc-vdu-order', vars)

    def _apply_device_templates(self, vdu):
        esc_name = self.service.esc
        action = self.root.ncs__devices\
                          .device[esc_name].apply_template
        for template in self.vnf_info.vdu[vdu.id].esc_device_template:
            input = action.get_input()

            def add_var(name, val):
                v = input.variable.create()
                v.name = name
                v.value = "'%s'" % (val)

            input.template_name = template.name
            add_var('TENANT', self.service.tenant)
            add_var('DEPLOYMENT_NAME', self.service.deployment_name)
            add_var('VM_GROUP_NAME', vm_group_name(self.vnf_info, vdu))

            for var in template.variable:
                add_var(var.name, var.value)
            action(input)

    def _create_nw(self, nw_ref):
        self._create_network(nw_ref.network_name, nw_ref)

    def _create_network(self, network_name, network_parameters):
        vars = ncs.template.Variables()
        vars.add('NETWORK', network_name)
        vars.add('DHCP', bool(network_parameters.dhcp))

        subnet = network_parameters.subnet

        self.log.info('network: %s' % (subnet.network))
        IPNetwork = internal_utils.IPNetwork(subnet.network)
        vars.add('IPV', IPNetwork.version)
        vars.add('SUBNET_ADDR', IPNetwork.address)
        vars.add('SUBNET_MASK', IPNetwork.subnet_mask)

        if subnet.name:
            vars.add('SUBNET_NAME', subnet.name)
        else:
            vars.add('SUBNET_NAME', "%s-subnet" % network_name)

        vars.add('SUBNET_GW', '')
        vars.add('SUBNET_NO_GW', '')
        if subnet.gateway:
            vars.add('SUBNET_GW', subnet.gateway)
        elif subnet.no_gateway:
            vars.add('SUBNET_NO_GW', True)

        vars.add('PROVIDER_NETWORK_TYPE', '')
        vars.add('PROVIDER_PHYSICAL_NETWORK', '')
        vars.add('PROVIDER_SEGMENTATION_ID', '')

        if network_parameters.vlan.id:
            vlan = network_parameters.vlan
            vars.add('PROVIDER_NETWORK_TYPE', 'vlan')
            vars.add('PROVIDER_PHYSICAL_NETWORK', vlan.physical_network_name)
            vars.add('PROVIDER_SEGMENTATION_ID', vlan.id)

        self.template.apply('tailf-etsi-rel2-nfvo-esc-deployment-network', vars)


class VNF(Service):
    """
    Implements the service point 'tailf-etsi-rel2-nfvo-esc-vnf-info', which is called when
    config is written to /nfvo-rel2:nfvo/vnf-info/esc/vnf-deployment .

    Do remember to look at the vnf_subcsriber to see how the vnf-deployment is
    handled.
    """
    @Service.create
    def cb_create(self, tctx, root, service, proplist):
        self.log.info("VNF")
        self.service = service
        self.root = root
        self.self_plan = PlanComponent(service, 'self', 'ncs:self')
        self.self_plan.append_state('ncs:init')
        self.self_plan.append_state('ncs:ready')
        self.self_plan.set_reached('ncs:init')

        vdu_plans = []
        any_failed = []
        self.log.debug("Running....")
        for vnf_info in service.vnf_info:
            validate_config(validate_vnfd_existance, (root, vnf_info.vnfd))
            vnfd = root.nfvo_rel2__nfvo.vnfd[vnf_info.vnfd]
            self.log.info('VNF %s' % (vnf_info.name))

            for vdu in vnfd_flavour_vdus(vnf_info, vnfd):
                validate_config(
                    validate_ned_for_managed_interface, (vnfd, vnf_info, vdu))
                (vdu_ready, failed) = \
                    self._apply_per_vdu_instance(vnfd, vnf_info, vdu)
                vdu_plans.append(vdu_ready)
                any_failed.append(failed)

        all_vdus_ready = all(vdu_plans) and len(vdu_plans) > 0
        if all_vdus_ready:
            self.self_plan.set_reached('ncs:ready')

        res_l = self.root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result
        dpr_key = (self.service.tenant, self.service.deployment_name, self.service.esc)
        if dpr_key in res_l:
            if res_l[dpr_key].status.error or any(any_failed):
                self.log.info("Failed getting to state 'ready' for {}".
                              format(dpr_key))
                self.self_plan.set_failed('ncs:ready')

    def _apply_per_vdu_instance(self, vnfd, vnf_info, vdu):
        plan_name = vnf_info.name + "-" + vdu.id
        plan = PlanComponent(self.service, plan_name, 'nfvo-rel2-esc:vdu')
        plan.append_state('ncs:init')
        plan.append_state('nfvo-rel2-esc:deployed')
        plan.append_state('ncs:ready')
        plan.set_reached('ncs:init')

        dpr_key = (self.service.tenant,
                   self.service.deployment_name,
                   self.service.esc)
        vdu_key = (vnf_info.name, vdu.id)
        res_l = self.root.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result
        (min_active, max_active) = _get_min_max_number_of_instances(vnfd, vnf_info, vdu)
        ready = False
        failed = False
        if dpr_key in res_l and vdu_key in res_l[dpr_key].vdu:
            vm_devices = res_l[dpr_key].vdu[vdu_key].vm_device
            deployed = sum(bool(d.status.deployed) for d in vm_devices) >= min_active
            ready = sum(bool(d.status.ready) for d in vm_devices) >= min_active
            any_error = any(bool(d.status.error) for d in vm_devices)
            if (deployed or ready) and not any_error:
                # we are always deployed if we are ready
                plan.set_reached('nfvo-rel2-esc:deployed')

            if ready:
                plan.set_reached('ncs:ready')

            if any_error:
                ready = False
                failed = True
                plan.set_failed('ncs:ready')

        return (ready, failed)


def _get_min_max_number_of_instances(vnfd, vnf_info, vdu):
    flavor = vnfd.deployment_flavor[vnf_info.vnfd_flavor]

    # Get start values from the vdu-profile
    min = 1
    max = 1
    if vdu.id in flavor.vdu_profile:
        min = flavor.vdu_profile[vdu.id].min_number_of_instances
        max = flavor.vdu_profile[vdu.id].max_number_of_instances

    # The instantiation level may override the minimal amount
    il = flavor.instantiation_level[vnf_info.instantiation_level]
    if vdu.id in il.vdu_level:
        min = il.vdu_level[vdu.id].number_of_instances

    return (min, max)


def validate_config(func, args):
    result, reason = func(*args)
    if not result:
        raise Exception(reason)


def validate_ned_for_managed_interface(vnfd, vnf_info, vdu):
    result = True
    reason = ""
    if vnf_info.vdu[vdu.id].managed:
        for ecp in vnfd.external_connection_point_descriptor:
            if ecp.management:
                for icp in vdu.internal_connection_point_descriptor:
                    if icp.external_connection_point_descriptor == ecp.id:
                        if not vdu.device_type:
                            result = False
                            reason = ('Must specify a'
                                      ' device-type (NED) for vdu %s in vnfd'
                                      ' %s when the vdu is managed' % (vdu.id, vnfd.id))
    return (result, reason)


def validate_vnfd_existance(root, vnf_id):
    try:
        root.nfvo_rel2__nfvo.vnfd[vnf_id]
        return (True, "")
    except KeyError:
        return (False, "It exists no VNFD with the name %s. Each VNF must have a corresponding VNFD." % vnf_id)


def vnfd_flavour_vdus(vnf_info, vnfd):
    profile_vdus = []
    flavor = vnf_info.vnfd_flavor
    for vnfd_flavor in vnfd.deployment_flavor:
        if flavor == vnfd_flavor.id:
            for vdu_profile in vnfd_flavor.vdu_profile:
                profile_vdus.append(vdu_profile.vdu)

    vdus = []
    for vdu in vnfd.vdu:
        if vdu.id in profile_vdus:
            vdus.append(vdu)

    return vdus


def vm_group_name(vnf_info, vdu):
    return "{}-{}".format(vnf_info.name, vdu.id)
