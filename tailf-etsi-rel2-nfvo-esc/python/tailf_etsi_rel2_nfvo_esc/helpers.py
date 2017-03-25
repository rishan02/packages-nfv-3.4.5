import ncs
import ncs.maapi as maapi
import ncs.maagic as maagic


class VNFError (RuntimeError):
    def __init__(self, tenant, deployment_name, esc):
        msg = "The deployment ({}, {}, {}) has failed".format(
            tenant, deployment_name, esc)
        super(RuntimeError, msg)


class NSError (RuntimeError):
    def __init__(self, ns_name):
        msg = "The NS {} has failed".format(ns_name)
        super(RuntimeError, msg)


def is_vnf_ready(tctx, tenant, deployment_name, esc):
    """Returns True if a VNF is ready.

    If the deployment has failed, a VNFError will be raised.

    Arguments:
    tctx -- a transaction context
    tenant -- Tenant name
    deployment_name -- The deployment name
    esc -- name of the ESC device
    """
    with maapi.single_read_trans(tctx.username, "system",
                                 db=ncs.OPERATIONAL) as oper_th:
        oroot = maagic.get_root(oper_th)
        vnf_deployment = oroot.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment

        if (tenant, deployment_name, esc) not in vnf_deployment:
            return False

        dep = vnf_deployment[tenant, deployment_name, esc]

        plan = dep.plan
        if plan.failed:
            raise VNFError(tenant, deployment_name, esc)

        ready_status = str(dep.plan.component['self'].state['ready'].status)
        return ready_status == 'reached'


def _vnf_devices(th, tenant, deployment_name, esc):
    oroot = maagic.get_root(th)
    res = oroot.nfvo_rel2__nfvo.vnf_info.esc.vnf_deployment_result[tenant,
                                                                   deployment_name,
                                                                   esc]

    devices = {}
    for vdu in res.vdu:
        devs = []
        for dev in vdu.vm_device:
            devs.append(dev.device_name)

        devices[(str(vdu.vnf_info), str(vdu.vdu))] = devs
    return devices


def vnf_devices(tctx, tenant, deployment_name, esc):
    """Returns a deployment's devices.

    The devices are return in map keyed (vnf-info, vdu) -> device list

    Arguments:
    tctx -- a transaction context
    tenant -- Tenant name
    deployment_name -- The deployment name
    esc -- name of the ESC device
    """
    with maapi.single_read_trans(tctx.username, "system",
                                 db=ncs.OPERATIONAL) as oper_th:
        return _vnf_devices(oper_th, tenant, deployment_name, esc)


def is_ns_ready(tctx, ns_name):
    """Returns True if a NS is ready.

    If the deployment has failed, a NSError will be raised.

    Arguments:
    tctx -- a transaction context
    ns_name -- the name of the NS Info
    """
    with maapi.single_read_trans(tctx.username, "system",
                                 db=ncs.OPERATIONAL) as oper_th:
        oroot = maagic.get_root(oper_th)
        ns_infos = oroot.nfvo_rel2__nfvo.ns_info.esc.ns_info

        if ns_name not in ns_infos:
            return False

        nsi = ns_infos[ns_name]

        plan = nsi.plan
        if plan.failed:
            raise NSError(ns_name)

        ready_status = str(nsi.plan.component['self'].state['ready'].status)
        return ready_status == 'reached'


def ns_devices(tctx, ns_name):
    """Returns a NS service's devices.

    The devices are return in map keyed (vnf-info, vdu) -> device list

    Arguments:
    tctx -- a transaction context
    ns_name -- The name of the NS Info
    """
    with maapi.single_read_trans(tctx.username, "system",
                                 db=ncs.OPERATIONAL) as oper_th:
        oroot = maagic.get_root(oper_th)
        ns_infos = oroot.nfvo_rel2__nfvo.ns_info.esc.ns_info
        nsi = ns_infos[ns_name]
        return _vnf_devices(oper_th, nsi.tenant, nsi.deployment_name, nsi.esc)
