package com.singtel.nfv.action;

import com.cisco.singtel.resourcemanager.ResourceAllocator;
import com.cisco.singtel.resourcemanager.base.Scope;
import com.cisco.singtel.resourcemanager.exceptions.IdPoolExhaustedException;
import com.cisco.singtel.resourcemanager.exceptions.IpPoolExhaustedException;
import com.singtel.sites.action.CreateHostActionBase;
import com.singtel.sites.model.Sites;
import com.singtel.sites.model.sites.sites.Site;
import com.singtel.sites.model.sites.sites.SiteList;
import com.singtel.sites.model.sites.sites.site.pods.Pod;
import com.singtel.sites.model.sites.sites.site.pods.PodList;
import com.singtel.sites.model.sites.sites.site.pods.pod.Dci;
import com.singtel.sites.model.sites.sites.site.pods.pod.dci.DciLink;
import com.singtel.sites.model.sites.sites.site.pods.pod.dci.dcilink.ServiceType;
import com.singtel.sites.PoolNameGenerator;
import com.singtel.nfv.model.vcpe.CreateVcpe;
import com.cisco.singtel.resourcemanager.util.BetterTemplateVariables;
import com.singtel.nfv.model.vcpe.createvcpe.Input;
import com.singtel.nfv.model.vcpe.createvcpe.Output;
import com.singtel.nfv.model.vcpe.createvcpe.input.AccessServiceType;
import com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlans;
import com.singtel.nfv.model.vcpe.createvcpe.input.RoutingProtocol;
import com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlans;
import com.singtel.nfv.model.vcpe.createvcpe.output.Result;
import com.singtel.nfv.model.vcpe.createvcpe.output.Errno;
import com.singtel.hostname.HostNameGenerator;
import com.tailf.conf.ConfException;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfTag;
import com.tailf.conf.ConfXMLParam;
import com.tailf.dp.DpActionTrans;
import com.tailf.dp.DpCallbackException;
import com.tailf.dp.annotations.ActionCallback;
import com.tailf.dp.proto.ActionCBType;
import com.tailf.ncs.ns.Ncs;
import com.tailf.ncs.template.Template;
import org.apache.log4j.Logger;
import se.dataductus.common.nso.channel.MaapiChannel;
import se.dataductus.common.nso.path.MaapiPath;

import java.io.IOException;

/**
 * Create vcpe RPC
 */
public class CreateVcpeAction extends CreateHostActionBase {
    private static final Logger LOGGER = Logger.getLogger(CreateVcpeAction.class);

    /**
     * Action callback
     *
     * @param transaction     Transaction
     * @param name            Name
     * @param kp              Keypath
     * @param inputParameters Input parameters
     * @return Output parameters
     * @throws DpCallbackException On failure
     */
    @ActionCallback(callPoint = CreateVcpe.ACTION_POINT_NAME, callType = ActionCBType.ACTION)
    public synchronized ConfXMLParam[] vcpeCreate(DpActionTrans transaction, ConfTag name, ConfObject[] kp, ConfXMLParam[] inputParameters) throws DpCallbackException {
        LOGGER.debug("CREATE_VCPE");
        boolean tenantCreated = false;
        String ban = "";
        Pod podObject = null;

        try (MaapiChannel serviceChannel = new MaapiChannel(transaction.getUserInfo());
             MaapiChannel tenantChannel = new MaapiChannel(transaction.getUserInfo())) {
            Input input = new Input(MaapiPath.fromPath("/" + name.toString()));
            input._fromConfXMLParams(inputParameters);

            AccessServiceType accessServiceType = input.accessServiceType().get();
            String serviceName = input.serviceName().get();
            String siteCode = input.siteCode().get();
            String podName = input.pod().get();
            String dciName = input.dci().get();
            String dciLinkEndPoint = input.dciLinkEndPoint().get();
            ban = input.ban().get();

            SiteList siteListObject = new Sites().sites().site();

            if (!siteListObject.elem(siteCode)._exists(serviceChannel)) {
                LOGGER.error(String.format("Site: %s doesn't exist.", siteCode));
                return createNgOutput(Errno.NO_SUCH_SITE, name);
            }

            Site siteObject = siteListObject.elem(siteCode)._read(serviceChannel, Scope.ALL_EXCEPT_LISTS);

            if (podName == null) {
                podName = siteListObject.elem(siteCode)._read(serviceChannel, Scope.LEAVES).preferredPod().get();
            }

            PodList podListObject = siteListObject.elem(siteCode).pods().pod();
            if (!podListObject.elem(podName)._exists(serviceChannel)) {
                LOGGER.error(String.format("Pod: %s doesn't exist.", podName));
                return createNgOutput(Errno.NO_SUCH_POD, name);
            }

            podObject = podListObject.elem(podName)._read(serviceChannel, Scope.ALL_EXCEPT_LISTS);

            if (dciName == null) {
                dciName = podObject.preferredDci().get();
            }

            Dci dciObject = podObject.dci().elem(dciName);
            if (!dciObject._exists(serviceChannel)) {
                LOGGER.error(String.format("Dci: %s doesn't exist.", dciName));
                return createNgOutput(Errno.NO_SUCH_DCI, name);
            }

            if (dciLinkEndPoint == null) {
                dciObject = dciObject._read(serviceChannel, Scope.LEAVES);

                switch (accessServiceType) {
                    case CPLUS:
                        dciLinkEndPoint = dciObject.preferredCplusLink().get();
                        break;
                    case MEGAPOP:
                        dciLinkEndPoint = dciObject.preferredMegapopLink().get();
                        break;
                    case SINGNET:
                        dciLinkEndPoint = dciObject.preferredSingnetLink().get();
                        break;
                }
            }

            DciLink link = findLinkEndpoint(serviceChannel, dciObject, dciLinkEndPoint);
            if (link == null) {
                return createNgOutput(Errno.NO_SUCH_LINK_ENDPOINT, name);
            }

            if (!verifyLinkEndpointServiceType(serviceChannel, link, ServiceType.fromString(accessServiceType.toString()))) {
                return createNgOutput(Errno.NO_SUCH_LINK_ENDPOINT_SERVICE_TYPE, name);
            }

            String escDeviceName = podObject.esc().get();
            MaapiPath datamodelPath = MaapiPath.goAs(true, Ncs.prefix, Ncs._devices_).go(Ncs._device_).elem(escDeviceName).go(Ncs._config_).go("esc", "esc_datamodel");
            if (!tenantChannel.exists(datamodelPath)) {
                tenantChannel.create(datamodelPath);
            }

            MaapiPath tenantPath = datamodelPath.go("tenants").go("tenant");
            if (!tenantChannel.exists(tenantPath.elem(ban))) {
                tenantChannel.startWriteTransaction();
                tenantChannel.create(tenantPath.elem(ban));
                tenantChannel.commit();
                tenantCreated = true;
            }

            synchronized (syncObject) {
                serviceChannel.startWriteTransaction();

                String hostName = HostNameGenerator.getHostName(input.sid().get());

                String mgmIpPoolName = PoolNameGenerator.managementIpPoolNameVcpe(siteObject, podObject);
                String mgmIpConsumer = PoolNameGenerator.managementIpConsumer(serviceName);
                try {
                    ResourceAllocator.getInstance().allocateIp(serviceChannel, mgmIpPoolName, mgmIpConsumer);
                } catch (IpPoolExhaustedException ie) {
                    if (tenantCreated) {
                        deleteCreatedTenant(transaction, ban, podObject);
                    }
                    LOGGER.error("MGMT-IP-POOL-EXHAUSTED", ie);
                    return createNgOutput(Errno.MGMT_IP_EXHAUSTED, name);
                }

                String vlanPoolName = PoolNameGenerator.vlanPoolName(siteObject, podObject, dciName, dciLinkEndPoint);

                try {
                    Long ingressId = input.ingressId().get();
                    Long egressId = input.egressId().get();

                    if (ingressId != null) {
                        ResourceAllocator.getInstance().allocateId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, true), ingressId.intValue());
                    } else {
                        ResourceAllocator.getInstance().allocateId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, true));
                    }

                    if (egressId != null) {
                        ResourceAllocator.getInstance().allocateId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, false), egressId.intValue());
                    } else {
                        ResourceAllocator.getInstance().allocateId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, false));
                    }
                } catch (IdPoolExhaustedException ve) {
                    if (tenantCreated) {
                        deleteCreatedTenant(transaction, ban, podObject);
                    }
                    LOGGER.error("VLAN-ID-EXHAUSTED: ", ve);
                    return createNgOutput(Errno.VLAN_ID_EXHAUSTED, name);
                }

                applyServiceTemplates(serviceChannel, input, accessServiceType, serviceName, siteCode, podName, dciName, dciLinkEndPoint);

                serviceChannel.commit();

                Output output = new Output(MaapiPath.fromPath("/" + name.toString()));
                output.result().set(Result.OK);
                output.hostname().set(hostName);
                output.managementIp().set(
                        ResourceAllocator.getInstance().getAllocatedIp(serviceChannel, mgmIpPoolName, mgmIpConsumer));
                output.ingressId().set(
                        ResourceAllocator.getInstance().getAllocatedId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, true)).longValue());
                output.egressId().set(
                        ResourceAllocator.getInstance().getAllocatedId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, false)).longValue());

                LOGGER.debug("DONE_CREATE_VCPE");
                return output._toConfXMLParams();
            }
        } catch (Exception e) {
            if (tenantCreated) {
                try {
                    deleteCreatedTenant(transaction, ban, podObject);
                } catch (IOException ioe) {
                    LOGGER.error("Failed to create vcpe", ioe);
                    return createNgOutput(Errno.UNEXPECTED_ERROR, name);
                }
            }
            LOGGER.error("Failed to create vcpe", e);
            return createNgOutput(Errno.UNEXPECTED_ERROR, name);
        }
    }

    private void applyCVlanTemplates(MaapiChannel serviceChannel, Input input, String serviceName) throws ConfException {
        for (LanCvlans lanCvlan : input.lanCvlans()) {
            BetterTemplateVariables variables = new BetterTemplateVariables();
            Template template = new Template(serviceChannel.getMaapi(), "l2nid-mpls-csr-service-lan-cvlan");

            variables.putQuoted("SERVICE_NAME", serviceName);
            variables.putQuoted("ID", lanCvlan.lanCvlanId().get());
            variables.putQuotedEmptyIfNull("IP_CIDR", lanCvlan.lanCvlanIp().get());

            template.apply(serviceChannel.getMaapi(), serviceChannel.getTransactionId(), new ConfPath("/"), variables);
        }

        for (WanCvlans wanCvlan : input.wanCvlans()) {
            BetterTemplateVariables variables = new BetterTemplateVariables();
            Template template = new Template(serviceChannel.getMaapi(), "l2nid-mpls-csr-service-wan-cvlan");

            variables.putQuoted("SERVICE_NAME", serviceName);
            variables.putQuoted("ID", wanCvlan.wanCvlanId().get());
            variables.putQuotedEmptyIfNull("IP_CIDR", wanCvlan.wanCvlanIp().get());

            template.apply(serviceChannel.getMaapi(), serviceChannel.getTransactionId(), new ConfPath("/"), variables);
        }
    }

    private void applyServiceTemplates(MaapiChannel serviceChannel,
                                       Input input,
                                       AccessServiceType accessServiceType,
                                       String serviceName,
                                       String siteCode,
                                       String podName,
                                       String dciName,
                                       String dciLinkEndPoint) throws ConfException {

        BetterTemplateVariables variables = new BetterTemplateVariables();
        Template template = new Template(serviceChannel.getMaapi(), "l2nid-mpls-csr-service");

        variables.putQuoted("SERVICE_NAME", serviceName);
        variables.putQuoted("ACCESS_SERVICE_TYPE", accessServiceType);
        variables.putQuoted("LAN_IP_CIDR", input.lanIp().get());
        variables.putQuoted("WAN_IP_CIDR", input.wanIp().get());
        variables.putQuoted("ROUTING_PROTOCOL", input.routingProtocol().get());
        variables.putQuoted("SITE_CODE", siteCode);
        variables.putQuoted("POD", podName);
        variables.putQuoted("DCI", dciName);
        variables.putQuoted("DCI_LINK_END_POINT", dciLinkEndPoint);
        variables.putQuoted("NFV_VAS_REFERENCE", input.nfvVasReference().get());
        variables.putQuoted("NFV_VAS_SCHEME", input.nfvVasScheme().get());
        variables.putQuoted("BRAND", input.brand().get());
        variables.putQuoted("NFV_SETUP", input.nfvSetup().get());
        variables.putQuoted("NFV_ACCESS_SETUP", input.nfvAccessSetup().get());
        variables.putQuoted("CIRCUIT_REFERENCE", input.circuitReference().get());
        variables.putQuoted("SPEED", input.speed().get());
        variables.putQuoted("PRODUCT_CODE", input.productCode().get());
        if (input.routingProtocol().get().equals(RoutingProtocol.BGP)) {
            variables.putQuotedEmptyIfNull("AS_NUMBER", input.asNumber().get());
            variables.putQuotedEmptyIfNull("REMOTE_AS_NUMBER", input.remoteAsNumber().get());
            variables.putQuotedEmptyIfNull("NEIGHBOR_PASSWD", input.neighborPasswd().get());
        } else {
            variables.putQuoted("AS_NUMBER", "");
            variables.putQuoted("REMOTE_AS_NUMBER", "");
            variables.putQuoted("NEIGHBOR_PASSWD", "");
        }
        variables.putQuotedEmptyIfNull("BRN", input.brn().get());
        variables.putQuotedEmptyIfNull("BAN", input.ban().get());
        variables.putQuotedEmptyIfNull("SID", input.sid().get());
        variables.putQuotedEmptyIfNull("GATEWAY_IP", input.gateway().get());
        variables.putQuotedEmptyIfNull("INGRESS_MAC_ADDRESS", input.ingressMacAddress().get());
        variables.putQuotedEmptyIfNull("EGRESS_MAC_ADDRESS", input.egressMacAddress().get());

        template.apply(serviceChannel.getMaapi(), serviceChannel.getTransactionId(), new ConfPath("/"), variables);

        applyCVlanTemplates(serviceChannel, input, serviceName);
    }

    private static ConfXMLParam[] createNgOutput(Errno errno, ConfTag name) throws DpCallbackException {
        try {
            Output output = new Output(MaapiPath.fromPath("/" + name.toString()));
            output.result().set(Result.NOK);
            output.errno().set(errno);
            return output._toConfXMLParams();
        } catch (ConfException ce) {
            throw new DpCallbackException("Failed to create vcpe: " + ce.getMessage());
        }
    }

    private void deleteCreatedTenant(DpActionTrans transaction, String ban, Pod podObject) throws IOException {
        try (MaapiChannel tenantChannel = new MaapiChannel(transaction.getUserInfo())) {
            tenantChannel.startWriteTransaction();

            MaapiPath tenantPath = MaapiPath.goAs(true, Ncs.prefix, Ncs._devices_).go(Ncs._device_).elem(podObject.esc().get())
                    .go(Ncs._config_).go("esc", "esc_datamodel").go("tenants").go("tenant");

            tenantChannel.delete(tenantPath.elem(ban));
            tenantChannel.commit();
        }
    }
}
