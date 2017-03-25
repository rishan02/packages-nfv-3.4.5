package com.cisco.singtel.dashboardUtm.callbacks;

import com.cisco.singtel.dashboardUtm.namespaces.utmDashboard;
import com.cisco.singtel.resourcemanager.base.Scope;
import com.cisco.singtel.resourcemanager.model.SingtelRm;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.vnfstatus.HostInformation;
import com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.vnfstatus.hostinformation.Status;
import com.cisco.singtel.resourcemanager.util.ConfUtil;
import com.cisco.singtel.dashboardUtm.model.Db;
import com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm;
import com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.DeploymentStatus;
import com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.OperationalStatus;

import com.singtel.dashboard.namespaces.dashboard;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfNoExists;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfValue;
import com.tailf.dp.DpCallbackException;
import com.tailf.dp.DpTrans;
import com.tailf.dp.annotations.DataCallback;
import com.tailf.dp.proto.DataCBType;
import org.apache.log4j.Logger;
import se.dataductus.common.nso.channel.MaapiChannel;
import se.dataductus.common.nso.path.MaapiPath;

/**
 * Class for operational status of utm in dashboard
 */
public class HostOperationalCallbacks {
    private static final Logger LOGGER = Logger.getLogger(HostOperationalCallbacks.class);
    /**
     * Get deployment status for utm.
     * @param trans transaction
     * @param kp keypath
     * @return ingress svlan-id
     * @throws DpCallbackException never
     */
    @DataCallback(callPoint = utmDashboard.callpoint_dashboard_utm_deployment_status, callType = DataCBType.GET_ELEM)
    public ConfValue deploymentStatusGet(DpTrans trans, ConfObject[] kp) throws DpCallbackException {

        try (MaapiChannel maapiChannel = new MaapiChannel(trans)) {
            Status status = getHostInformationStatus(maapiChannel, kp);
            if (status == null) {
                return new ConfNoExists();
            }
            switch(status) {
                case IN_PROGRESS:
                    return DeploymentStatus.CONVERTER.convertTo(DeploymentStatus.IN_PROGRESS);
                case ERROR_DEPLOYED:
                    return DeploymentStatus.CONVERTER.convertTo(DeploymentStatus.FAILED);
                default:
                    return DeploymentStatus.CONVERTER.convertTo(DeploymentStatus.READY);
            }
        } catch (Exception e) {
            LOGGER.error("Could not get deployment-status", e);
            return new ConfNoExists();
        }
    }

    /**
     * Get operational status for utm
     * @param trans transaction
     * @param kp keypath
     * @return ingress svlan-id
     * @throws DpCallbackException never
     */
    @DataCallback(callPoint = utmDashboard.callpoint_dashboard_utm_operational_status, callType = DataCBType.GET_ELEM)
    public ConfValue operationaltStatusGet(DpTrans trans, ConfObject[] kp) throws DpCallbackException {

        try (MaapiChannel maapiChannel = new MaapiChannel(trans)) {
            Status status = getHostInformationStatus(maapiChannel, kp);
            if (status == null) {
                return new ConfNoExists();
            }
            switch(status) {
                case IN_PROGRESS:
                    return OperationalStatus.CONVERTER.convertTo(OperationalStatus.IN_PROGRESS);
                case RECOVERING:
                    return OperationalStatus.CONVERTER.convertTo(OperationalStatus.RECOVERING);
                case ERROR_RECOVERING:
                case ERROR_DEPLOYED:
                    return OperationalStatus.CONVERTER.convertTo(OperationalStatus.FAILED);
                default:
                    return OperationalStatus.CONVERTER.convertTo(OperationalStatus.READY);
            }
        } catch (Exception e) {
            LOGGER.error("Could not get operational-status", e);
            return new ConfNoExists();
        }
    }

    private Status getHostInformationStatus(MaapiChannel channel, ConfObject[] kp) {
        ConfKey brn = ConfUtil.findKeyOrThrow(dashboard._customer_, kp );
        ConfKey nfv = ConfUtil.findKeyOrThrow(dashboard._service_, kp );
        MaapiPath service = new Db().customers().customer().elem(brn).services().service().elem(nfv).utm()._getPath().go(Utm.LEAF_HOSTNAME_TAG);
        String hostname = channel.getElem(service).toString();
        HostInformation hostInfo = new SingtelRm().resourceManager().vnfStatus().hostInformation().elem(hostname)._read(channel, Scope.LEAVES);
        return hostInfo.status().get();
    }
}
