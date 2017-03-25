# -*- mode: python; python-indent: 4 -*-
from ncs.application import Application
import tailf_etsi_rel2_nfvo_esc.vnf_info as vnf_info
import tailf_etsi_rel2_nfvo_esc.ns_info as ns_info
import tailf_etsi_rel2_nfvo_esc.onboarding as onboarding
import tailf_etsi_rel2_nfvo_esc.vnf_subscriber as vnf_subscriber
import tailf_etsi_rel2_nfvo_esc.netconf_notif as netconf_notif
import tailf_etsi_rel2_nfvo_esc.staged_delete as staged_delete
import tailf_etsi_rel2_nfvo_esc.settings as settings


class TailF_NFVO_ESC(Application):
    def setup(self):
        self.log.info('TailF_NFVO_ESC RUNNING')
        self.register_service('tailf-etsi-rel2-nfvo-esc-vnf-info-backend', vnf_info.VNFBackend)
        self.register_service('tailf-etsi-rel2-nfvo-esc-vnf-info', vnf_info.VNF)
        self.register_service('tailf-etsi-rel2-nfvo-esc-ns-info', ns_info.NSInfo)

        self.register_service('tailf-etsi-rel2-nfvo-esc-onboard-flavor', onboarding.Flavor)
        self.register_service('tailf-etsi-rel2-nfvo-esc-onboard-image', onboarding.Image)

        self.register_service('tailf-etsi-rel2-nfvo-esc-settings', settings.Settings)

        self.vnf_sub = vnf_subscriber.VNFSubscriber(self)

        self.vnf_sub.start()

        self.nc_sub = netconf_notif.NetconfNotifSub(self)
        self.nc_sub.start()

        self.sd_sub = staged_delete.StagedDelete(self)
        self.sd_sub.start()

    def teardown(self):
        self.sd_sub.stop()
        self.nc_sub.stop()
        self.vnf_sub.stop()

        self.log.info('Worker FINISHED')
