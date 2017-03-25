# -*- mode: python; python-indent: 4 -*-
import ncs
import ncs.experimental
import ncs.maapi as maapi
import ncs.maagic as maagic

from internal_utils import is_ha_slave


class StagedDelete(ncs.experimental.Subscriber):
    """
    This subscriber subscribes to changes in the '/license-handling/revoked'
    container and will, when triggered, pick up the device and the ESC service
    in order to remove them. It will also delete the device from the 'revoked'
    container.
    """

    def init(self):
        self.register('/nfvo-rel2:nfvo/nfvo-rel2-esc-sd:staged-delete-esc/nfvo-rel2-esc-sd:deleted')

    def should_iterate(self):
        if is_ha_slave():
            self.log.debug("Stage deletion: HA role is slave, skipping iteration")
            return False
        else:
            return True

    def pre_iterate(self):
        # Initiate state and return it
        return []

    def iterate(self, kp, op, oldv, newv, state):
        if op == ncs.MOP_CREATED:
            key = (str(kp[0][0]), str(kp[0][1]), str(kp[0][2]))
            state.append(key)

    # This will run in a separate thread
    def post_iterate(self, state):
        self.log.info('Staged Delete: RUNNING , state=' + str(state))

        context = "system"

        with maapi.single_write_trans("", context) as th:
            for k in state:
                deleted = maagic.get_node(th,
                                          '/nfvo-rel2:nfvo/nfvo-rel2-esc-sd:staged-delete-esc/nfvo-rel2-esc-sd:deleted')
                with maapi.single_write_trans(deleted[k].username, context) as uth:
                    vnf_svcs = maagic.get_node(uth,
                                               '/nfvo-rel2:nfvo/nfvo-rel2-esc:tailf-esc-internal'
                                               '/nfvo-rel2-esc:vnf-deployment')

                    # Delete the VNF Service
                    del vnf_svcs[k]

                    # Find all devices
                    dep_res = maagic.get_node(uth, '/nfvo-rel2:nfvo/nfvo-rel2:vnf-info/nfvo-rel2-esc:esc/'
                                              'nfvo-rel2-esc:vnf-deployment-result')
                    if k in dep_res:
                        for vdu in dep_res[k].vdu:
                            for vmd in vdu.vm_device:
                                device_name = vmd.device_name
                                self.log.info('device_name: %s' % (device_name))
                                devices = maagic.get_node(uth, "/ncs:devices/ncs:device")
                                del devices[device_name]

                        # delete the deployment result
                        del dep_res[k]
                    uth.apply()
                del deleted[k]
                th.apply()

    # determine if post_iterate() should run
    def should_post_iterate(self, state):
        return state != []
