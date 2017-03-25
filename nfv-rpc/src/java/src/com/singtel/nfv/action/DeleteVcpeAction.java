package com.singtel.nfv.action;

import com.cisco.singtel.resourcemanager.ResourceAllocator;
import com.cisco.singtel.resourcemanager.base.Scope;
import com.singtel.nfv.model.vcpe.DeleteVcpe;
import com.singtel.nfv.model.vcpe.deletevcpe.Input;
import com.singtel.nfv.model.vcpe.deletevcpe.Output;
import com.singtel.nfv.model.vcpe.deletevcpe.output.Result;
import com.singtel.nfv.model.vcpe.deletevcpe.output.Errno;
import com.singtel.sites.model.nfv.Megapop;
import com.singtel.sites.model.nfv.megapop.L2nidMplsCsr;

import com.singtel.sites.PoolNameGenerator;
import com.tailf.conf.ConfException;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfTag;
import com.tailf.conf.ConfXMLParam;
import com.tailf.dp.DpActionTrans;
import com.tailf.dp.DpCallbackException;
import com.tailf.dp.annotations.ActionCallback;
import com.tailf.dp.proto.ActionCBType;
import org.apache.log4j.Logger;
import se.dataductus.common.nso.channel.MaapiChannel;
import se.dataductus.common.nso.path.MaapiPath;

public class DeleteVcpeAction {
    private static final Logger LOGGER = Logger.getLogger(DeleteVcpeAction.class);

    /**
     * DeleteVcpeAction callback
     * @param transaction
     * @param name
     * @param kp
     * @param input
     * @return Output parameters
     * @throws DpCallbackException
     */
    @ActionCallback(callPoint = DeleteVcpe.ACTION_POINT_NAME, callType = ActionCBType.ACTION)
    public synchronized ConfXMLParam[] vcpeDelete(DpActionTrans transaction, ConfTag name, ConfObject[] kp, ConfXMLParam[] input) throws DpCallbackException {

        LOGGER.debug("BEGIN DELETE_VCPE_ACTION");

        try (MaapiChannel serviceChannel = new MaapiChannel(transaction.getUserInfo())) {
            serviceChannel.startWriteTransaction();
            Input inputModel = new Input(MaapiPath.fromPath("/" + name.toString()));
            inputModel._fromConfXMLParams(input);

            String serviceName = inputModel.serviceName().get();

            //
            // Verify service instance exists
            //
            L2nidMplsCsr l2nidMplsCsr = new Megapop(MaapiPath.fromPath("/nfv:megapop")).l2nidMplsCsr().elem(serviceName);
            if (!l2nidMplsCsr._exists(serviceChannel)) {
                LOGGER.error(String.format("Failed to delete vCPE, service %s not found.", serviceName));
                return createNgOutput(Errno.UNEXPECTED_ERROR, name);
            }
            l2nidMplsCsr._read(serviceChannel, Scope.LEAVES);

            // Read values
            String siteObject = l2nidMplsCsr.siteCode().get();
            String podObject = l2nidMplsCsr.pod().get();
            String dciLinkEndPoint = l2nidMplsCsr.dciLinkEndPoint().get();
            String dciName = l2nidMplsCsr.dci().get();

            // Release allocated resources
            String vlanPoolName = PoolNameGenerator.vlanPoolName(siteObject, podObject, dciName, dciLinkEndPoint);
            String mgmIpPoolName = PoolNameGenerator.managementIpPoolNameVcpe(siteObject, podObject);
            String mgmIpConsumer = PoolNameGenerator.managementIpConsumer(serviceName);
            ResourceAllocator.getInstance().deallocateId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, true));
            ResourceAllocator.getInstance().deallocateId(serviceChannel, vlanPoolName, PoolNameGenerator.vlanIdConsumer(serviceName, false));
            ResourceAllocator.getInstance().deallocateIp(serviceChannel, mgmIpPoolName, mgmIpConsumer);

            // Delete service instance
            l2nidMplsCsr._delete();
            l2nidMplsCsr._write(serviceChannel);

            // Commit and report result
            serviceChannel.commit();
            Output output = new Output(MaapiPath.fromPath("/" + name.toString()));
            output.result().set(Result.OK);
            output._create();
            output.successMessage().set("vCPE deleted");
            return output._toConfXMLParams();

        } catch (Exception e) {
            LOGGER.error("Failed to delete vcpe", e);
            try {
                return createNgOutput(Errno.UNEXPECTED_ERROR, name);
            } catch (ConfException ce) {
                throw new DpCallbackException("Failed to delete vCPE: " + ce.getMessage());
            }
        } finally {
            LOGGER.debug("END DELETE_VCPE_ACTION");
        }
    }

    private ConfXMLParam[] createNgOutput(Errno errno, ConfTag name) throws ConfException {
        Output output = new Output(MaapiPath.fromPath("/" + name.toString()));
        output.result().set(Result.NOK);
        output._create();
        output.errno().set(errno);
        return output._toConfXMLParams();
    }
}

