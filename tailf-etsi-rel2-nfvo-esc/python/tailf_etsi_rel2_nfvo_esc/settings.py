from ncs.application import Service
import ncs


class Settings(Service):
    @Service.create
    def cb_create(self, tctx, root, service, proplist):
        template = ncs.template.Template(service)
        template.apply('tailf-etsi-rel2-nfvo-esc-notif-subscription')
