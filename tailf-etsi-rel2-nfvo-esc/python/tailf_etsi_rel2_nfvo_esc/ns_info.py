import ncs
from ncs.application import Service, PlanComponent


class NSInfo(Service):
    @Service.create
    def cb_create(self, tctx, root, service, proplist):
        self.tctx = tctx
        self.root = root
        self.service = service
        self.proplist = proplist
        self.template = ncs.template.Template(self.service)
        self.log.info("ns-info")
        self.self_plan = PlanComponent(service, 'self', 'ncs:self')
        self.self_plan.append_state('ncs:init')
        self.self_plan.append_state('ncs:ready')
        self.self_plan.set_reached('ncs:init')

        self.nsd = root.nfvo_rel2__nfvo.nsd[service.nsd]
        if not service.flavor:
            raise Exception("Must specify NSD deployment flavor")
        self.log.info("NSD deployment-flavor {}".format(service.flavor))
        self.deployment_flavor = self.nsd.deployment_flavor[service.flavor]

        # Find the scaling aspect and from it, the instantiation level

        if not service.instantiation_level:
            raise Exception("Must specify instantiation level")
        inst_level = self.deployment_flavor.instantiation_level[service.instantiation_level]

        for vl2level in inst_level.vl_to_level_mapping:
            self._create_vl(vl2level)

        for vnf2level in inst_level.vnf_to_level_mapping:
            self._create_vnf(vnf2level)

        with ncs.maapi.single_read_trans(tctx.username, tctx.context,
                                         db=ncs.OPERATIONAL) as oper_th:
            oroot = ncs.maagic.get_root(oper_th)
            vnf_deployment = oroot.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment
            if not (service.tenant, service.deployment_name, service.esc) in vnf_deployment:
                self.log.info("VNF not yet ready")
                return
            dep = oroot.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment[service.tenant, service.deployment_name, service.esc]
            plan = dep.plan
            self.copy_vdu_plans(plan)
            if plan.failed:
                self.log.error("VNF %s/%s/%s has failed" % (service.tenant,
                                                            service.deployment_name,
                                                            service.esc))
                self.self_plan.set_failed('ncs:ready')
                return
            ready_status = str(plan.component['self'].state['ready'].status)
            if 'reached' != ready_status:
                self.log.info("VNF not yet ready")
                return

            self.self_plan.set_reached('ncs:ready')

    def _create_vnf(self, vnf2level):
        self.log.info("_create_vnf %s" % (vnf2level.vnf_profile))
        vnf_profile = self.deployment_flavor.vnf_profile[vnf2level.vnf_profile]
        vnfd_name = vnf_profile.vnfd
        vnfd_deployment_flavor = vnf_profile.flavor
        vnfd_instantiation_level = vnf_profile.instantiation_level
        self.log.info("_create_vnf %s/%s/%s" % (vnfd_name, vnfd_deployment_flavor, vnfd_instantiation_level))
        # First copy all VDU settings
        vars = ncs.template.Variables()
        vars.add('VNF_PROFILE_NAME', vnf_profile.id)
        vars.add('VNF_NAME', vnfd_name)
        vars.add('VNF_FLAVOR', vnfd_deployment_flavor)
        vars.add('VNF_IL', vnfd_instantiation_level)
        self.template.apply('tailf-etsi-rel2-nfvo-esc-ns-info-copy-to-vnf-info', vars)

        # Setup the VNF's interfaces
        for sapd_con in vnf_profile.sapd_connectivity:
            sapd_name = sapd_con.sapd

            # Figure out the network name for this sapd
            network_name = self.service.sap_info[sapd_name].network_name
            vnf_cp = sapd_con.cp
            self.log.debug("SAPD: %s <-> %s on VLR %s" % (sapd_name, vnf_cp, network_name))
            vars.add('VNF_CP', vnf_cp)
            vars.add('NETWORK_NAME', network_name)
            self.template.apply('tailf-etsi-rel2-nfvo-esc-ns-info-vnfd-connection-point', vars)
        for vl_con in vnf_profile.virtual_link_connectivity:
            vlp_name = vl_con.virtual_link_profile
            vl_profile = self.deployment_flavor.virtual_link_profile[vlp_name]
            vld_name = vl_profile.virtual_link_descriptor
            vlr = self._vlr_name(vld_name)
            vnf_cp = vl_con.cp
            self.log.debug("VLP: %s <-> %s on VLR %s" % (vld_name, vnf_cp, vlr))
            vars.add('VNF_CP', vnf_cp)
            vars.add('NETWORK_NAME', vlr)
            self.template.apply('tailf-etsi-rel2-nfvo-esc-ns-info-vnfd-connection-point', vars)

    def _create_vl(self, vl2level):
        vlp_name = vl2level.virtual_link_profile
        self.log.info("_create_vl %s" % (vlp_name))
        vl_profile = self.deployment_flavor.virtual_link_profile[vlp_name]
        vl_info = self.service.virtual_link_info[vl_profile.virtual_link_descriptor]

        vars = ncs.template.Variables()
        vars.add('NW', self._vlr_name(vl_info.virtual_link_descriptor))
        vars.add('NS_VLD', vl_info.virtual_link_descriptor)
        self.template.apply('tailf-etsi-rel2-nfvo-esc-ns-info-nw', vars)

    def _vlr_name(self, ns_vld_name):
        vlr = "%s-%s-%s" % (self.service.tenant,
                            self.service.deployment_name,
                            ns_vld_name)
        return vlr

    def copy_vdu_plans(self, vnfr_plan):
        for vdu_plan in vnfr_plan.component:
            if 'self' == str(vdu_plan.name):
                continue

            plan = PlanComponent(self.service, vdu_plan.name, 'nfvo-rel2-esc:vdu')
            plan.append_state('ncs:init')
            plan.append_state('nfvo-rel2-esc:deployed')
            plan.append_state('ncs:ready')
            for state in vdu_plan.state:
                if 'reached' == str(state.status):
                    plan.set_reached(state.name)
                elif 'failed' == str(state.status):
                    plan.set_failed(state.name)
