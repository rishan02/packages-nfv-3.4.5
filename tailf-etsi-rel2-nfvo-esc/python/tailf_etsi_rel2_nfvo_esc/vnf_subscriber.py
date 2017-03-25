import ncs
import ncs.experimental
import ncs.maapi as maapi
import ncs.maagic as maagic

from internal_utils import is_ha_slave

class VNFSubscriber(ncs.experimental.Subscriber):
    def init(self):
        self.register('/nfvo-rel2:nfvo/nfvo-rel2:vnf-info/nfvo-rel2-esc:esc/nfvo-rel2-esc:vnf-deployment')

    def pre_iterate(self):
        return ([], [])

    def should_iterate(self):
        if is_ha_slave():
            self.log.debug("VNFSubscriber: HA role is slave, skipping iteration")
            return False
        else:
            return True

    def iterate(self, kp, op, oldv, newv, state):
        if op == ncs.MOP_CREATED or op == ncs.MOP_MODIFIED:
            key = (str(kp[0][0]), str(kp[0][1]), str(kp[0][2]))
            state[0].append(key)
        elif op == ncs.MOP_DELETED:
            key = (str(kp[0][0]), str(kp[0][1]), str(kp[0][2]))
            state[1].append(key)
        return ncs.ITER_CONTINUE

    def should_post_iterate(self, state):
        return state[0] != [] or state[1] != []

    def post_iterate(self, state):
        context = "system"

        added = state[0]
        removed = state[1]
        with maapi.single_read_trans("", context) as outer:
            for key in added:
                kp = '/nfvo-rel2:nfvo/nfvo-rel2:vnf-info/nfvo-rel2-esc:esc/nfvo-rel2-esc:vnf-deployment{"%s" "%s" "%s"}' % key
                self.log.info("VNFSubscriber, stored kp: %s" % (kp))
                dep = maagic.get_node(outer, kp)
                username = dep.username
                # Switch usercontext to the stored username
                with maapi.single_write_trans(username, context) as th:
                    dep = maagic.get_node(th, kp)
                    template = ncs.template.Template(dep)
                    # Write to /nfvo/tailf-esc-internal
                    template.apply('tailf-etsi-rel2-nfvo-esc-vnf-info-copy')
                    # Delete any possible residue in staged delete for
                    # this vnf
                    self._delete_residue_staged_delete(th, key, dep)
                    try:
                        th.apply()
                    except Exception as e:
                        self.log.error("Received %s when commiting %s" % (e, key))
                        self._handle_commit_error(username, context, key, e.message)

            for key in removed:
                svc_kp = '/nfvo-rel2:nfvo/nfvo-rel2-esc:tailf-esc-internal/nfvo-rel2-esc:vnf-deployment{"%s" "%s" "%s"}'\
                    % key

                if outer.exists(svc_kp):
                    svc = maagic.get_node(outer, svc_kp)
                    username = str(svc.username)
                    staged_delete = svc.nfvo_behaviors.staged_delete
                else:
                    # If not service is not found, we may still need to clean up the oper data
                    username = "admin"
                    staged_delete = False

                self.log.info("Delete %s" % (svc_kp))
                with maapi.single_write_trans(username, context) as th:
                    if staged_delete:
                        self.log.info("Using staged delete for %s/%s/%s" % key)
                        delete_kp = "/nfvo-rel2:nfvo/nfvo-rel2-esc-sd:staged-delete-esc/nfvo-rel2-esc-sd:delete"
                    else:
                        self.log.info("Skipping staged delete for %s/%s/%s" % key)
                        delete_kp = "/nfvo-rel2:nfvo/nfvo-rel2-esc-sd:staged-delete-esc/nfvo-rel2-esc-sd:deleted"
                    delete_list = maagic.get_node(th, delete_kp)
                    delete = delete_list.create(key)
                    delete.username = username
                    th.apply()

    def _handle_commit_error(self, username, context, key, error_msg):
        with maapi.single_write_trans(username, context, db=ncs.OPERATIONAL) as th:
            results = maagic.get_root(th).nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result
            res = results.create(key)
            res.status.error = error_msg
            th.apply()

    def _delete_residue_staged_delete(self, th, key, dep):
        #super small optimization
        if dep.nfvo_behaviors.staged_delete:
            kp = '/nfvo-rel2:nfvo/nfvo-rel2-esc-sd:staged-delete-esc/'
            staged_delete_node = maagic.get_node(th, kp + "delete")
            staged_deleted_node = maagic.get_node(th, kp + "deleted")
            del staged_delete_node[key]
            del staged_deleted_node[key]
