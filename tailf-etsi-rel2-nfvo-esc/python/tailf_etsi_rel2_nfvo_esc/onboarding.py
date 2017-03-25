from ncs.application import Service
import ncs

"""
   Implementation of the servicepoints:
      tailf-etsi-rel2-nfvo-esc-onboard-image
      tailf-etsi-rel2-nfvo-esc-onboard-flavor
"""


class Flavor(Service):
    """
    Service code to handle the tailf-etsi-rel2-nfvo-esc-onboard-flavor.
    """
    @Service.create
    def cb_create(self, tctx, root, service, proplist):
        self.template = ncs.template.Template(service)

        vnfd = root.nfvo_rel2__nfvo.vnfd[service.vnfd]
        vdu = vnfd.vdu[service.vdu]

        if (vdu.virtual_compute_descriptor):
            vars = ncs.template.Variables()
            vcd = vnfd.virtual_compute_descriptor[vdu.virtual_compute_descriptor]

            memGb = float(vcd.virtual_memory.virtual_memory_size)

            vars.add('MEM', int(memGb * 1024))
            vars.add('VCPU', vcd.virtual_cpu.
                     number_of_virtual_cpus)
            vars.add('ROOTDISK', 0)
            vars.add('SWAPDISK', 0)
            vars.add('EPHEMERALDISK', 0)

            if vdu.virtual_storage_descriptor:
                for s_name in vdu.virtual_storage_descriptor:
                    storage = vnfd.virtual_storage_descriptor[s_name]
                    tos = storage.type_of_storage
                    size = storage.size_of_storage * 1024
                    if tos == 'root':
                        vars.add('ROOTDISK', size)
                    elif tos == 'swap':
                        vars.add('SWAPDISK', size)
                    elif tos == 'ephemeral':
                        vars.add('EPHEMERALDISK', size)

            self.template.apply('tailf-etsi-rel2-nfvo-esc-onboarding-flavor', vars)
        else:
            self.log.info("No virtual compute provided skipping")


class Image(Service):
    """
    Service code to handle the tailf-etsi-rel2-nfvo-esc-onboard-image.
    """
    @Service.create
    def cb_create(self, tctx, root, service, proplist):
        self.template = ncs.template.Template(service)

        vnfd = root.nfvo_rel2__nfvo.vnfd[service.vnfd]
        vdu = vnfd.vdu[service.vdu]

        vars = ncs.template.Variables()

        vars.add('SRC', vdu.software_image_descriptor.image)
        vars.add('DISK_FORMAT', vdu.software_image_descriptor.disk_format)
        vars.add('CONTAINER_FORMAT', vdu.software_image_descriptor.container_format)

        esc_additional = [
            'serial_console',
            'e1000_net',
            'virtio_net',
            'disk_bus']
        for add in esc_additional:
            if add in vdu.software_image_descriptor.additional_setting:
                vars.add(add, vdu.software_image_descriptor.additional_setting[add].value)
            else:
                vars.add(add, '')

        self.template.apply('tailf-etsi-rel2-nfvo-esc-onboarding-image', vars)
