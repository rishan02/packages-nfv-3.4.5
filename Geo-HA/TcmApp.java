package com.tailf.ns.tailfHcc;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tailf.cdb.Cdb;
import com.tailf.cdb.CdbDBType;
import com.tailf.cdb.CdbDiffIterate;
import com.tailf.cdb.CdbLockType;
import com.tailf.cdb.CdbSession;
import com.tailf.cdb.CdbSubscription;
import com.tailf.cdb.CdbSubscriptionSyncType;
import com.tailf.conf.ConfBool;
import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfEnumeration;
import com.tailf.conf.ConfException;
import com.tailf.conf.ConfIPPrefix;
import com.tailf.conf.ConfInt32;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfList;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfTag;
import com.tailf.conf.ConfValue;
import com.tailf.conf.DiffIterateFlags;
import com.tailf.conf.DiffIterateOperFlag;
import com.tailf.conf.DiffIterateResultFlag;
import com.tailf.ncs.ApplicationComponent;
import com.tailf.ncs.ResourceManager;
import com.tailf.ncs.annotations.Resource;
import com.tailf.ncs.annotations.ResourceType;
import com.tailf.ncs.annotations.Scope;
import com.tailf.ns.tailfHcc.namespaces.tailfHcc;

////
//
// tcm Application
//
// Tail-f Cluster Manager
//
//    This class is used to subscribe to changes in the
//    cluster management model. Implemented as an application
//    the resource manager is used for Cdb sockets.
//
////
public class TcmApp
    implements ApplicationComponent
{
    private static final Logger log =
        Logger.getLogger(TcmApp.class);

    ////
    // Use the resource manager to inject Cdb sockets
    // for us.
    ////
    @Resource ( type = ResourceType.CDB, scope=Scope.INSTANCE,
                qualifier="sub-sock")
                private Cdb cdbSubSocket;

    @Resource ( type = ResourceType.CDB,
                scope=Scope.INSTANCE,
                qualifier="data-sock")
                private Cdb cdbDataSocket;

    boolean shouldRun;
    private CdbSubscription cdbSubscription;

    ////
    // Periodic timer used to update active slaves
    ////
    private static Timer timer;

    ////
    // Iterator
    ////
    class DiffIterateTcm implements CdbDiffIterate {

        private Logger log =
            Logger.getLogger(DiffIterateTcm.class);

        public DiffIterateResultFlag  iterate ( ConfObject[] kp,
                                                DiffIterateOperFlag op,
                                                ConfObject oldValue,
                                                ConfObject newValue,
                                                Object initstate )
            {

                try {
                    CdbSession cdbSession = (CdbSession)initstate;
                    ConfTag tag;
                    switch ( op ) {

                    case MOP_CREATED: {
                        // If the length is 1, its the creation of /ha container
                        if (kp.length == 1) {
                            readInitTcmCfg(cdbSession);
                            return DiffIterateResultFlag.ITER_CONTINUE;
                        }
                        //If the length is 2, its /ha/vip or /ha/bgp,
                        ///ha/vip is handled by tailf_viperl, so its
                        //ignored here
                        else if (kp.length == 2) {
                            tag = (ConfTag)kp[0];
                            switch( tag.getTagHash() ) {
                            case tailfHcc._bgp:
                                readTcmClusterParameters(cdbSession);
                                ActivateBGP a = new ActivateBGP();
                                Thread t = new Thread(a);
                                t.start();
                                break;
                            default:
                                break;
                            }
                            return DiffIterateResultFlag.ITER_CONTINUE;
                        }
                        //If the length is 3, its /ha/member, kp[0] should be
                        //ConfKey
                        else if (kp.length == 3) {
                            tag = (ConfTag)kp[1];

                            //What was created ?
                            switch( tag.getTagHash() ) {

                            case tailfHcc._member:
                                readTcmClusterMemCfg(cdbSession, (ConfKey)kp[0]);
                                break;

                            default:
                                break;
                            }
                            return DiffIterateResultFlag.ITER_RECURSE;
                        }
                    }
                    case MOP_DELETED: {
                        // /ha was deleted
                        if (kp.length == 1) {
                            timer.cancel();
                            Deactivate d = new Deactivate();
                            Thread t = new Thread(d);
                            t.start();
                            return DiffIterateResultFlag.ITER_CONTINUE;
                        }
                        if (kp.length == 2) {
                            tag = (ConfTag)kp[0];
                            switch( tag.getTagHash() ) {
                            case tailfHcc._bgp:
                                Cluster.setBgpAnycastEnabled(false);
                                DeactivateBGP d = new DeactivateBGP();
                                Thread t = new Thread(d);
                                t.start();
                                break;
                            default:
                                break;
                            }
                            return DiffIterateResultFlag.ITER_CONTINUE;
                        }
                        // /ha/member was deleted
                        else {
                            tag = (ConfTag) kp[1];
                            switch( tag.getTagHash() ) {

                            case tailfHcc._member:
                                removeTcmClusterMemCfg((ConfKey)kp[0]);
                                break;

                            default:
                                break;
                            }
                            return DiffIterateResultFlag.ITER_CONTINUE;
                        }
                    }
                    case MOP_MODIFIED: {
                        tag = (ConfTag) kp[1];
                        switch( tag.getTagHash() ) {
                        case tailfHcc._member:
                            readTcmClusterMemCfg(cdbSession, (ConfKey)kp[0]);
                            break;

                        default:
                            break;
                        }

                        return DiffIterateResultFlag.ITER_RECURSE;
                    }

                    case MOP_VALUE_SET: {
                        tag = (ConfTag) kp[0];
                        switch (tag.getTagHash()) {

                        case tailfHcc._token:
                        case tailfHcc._local_user:
                        case tailfHcc._failure_limit:
                        case tailfHcc._bgp:
                        case tailfHcc._anycast_prefix:
                        case tailfHcc._anycast_path_min:
                            readTcmClusterParameters(cdbSession);
                            break;

                        case tailfHcc._interval:
                            readTcmClusterParameters(cdbSession);
                            resetTimer();
                            break;
                        default:
                            break;
                        }
                        return DiffIterateResultFlag.ITER_RECURSE;
                    }
                    default:
                        break;
                    }
                }
                catch (Exception e) {
                    log.error ("Diff Iterate Error ", e);
                }

                return DiffIterateResultFlag.ITER_CONTINUE;
            }
    }

    void readInitTcmCfg(CdbSession cdbSession) {
        try {
            ////
            // Intialize the Cluster database
            ////
            Cluster.init();
        }
        catch (Exception e) {
            throw new RuntimeException("Fail to initalize HA cluster", e);
           
        }

        readTcmCfg(cdbSession);
        try {

            Cluster.haInitialize();
            ////
            // Determine if the node has already been activated and
            // there is already an valid role. If so assume that role
            ////
            if (Cluster.haRestartRecovery() == false) {
                ModifyActivation m = new ModifyActivation();
                Thread t = new Thread(m);
                t.start();
            }
        }
        catch (Exception e) {
            log.error("NCS HA is likely not enabled");
            throw new RuntimeException("Fail to initalize HA", e);
        }
        setTimer();
    }

    ////
    // Remove Cluster member config
    ////
    void removeTcmClusterMemCfg(ConfKey key)
        {
            Cluster.deleteMember(key.toString());
        }

    ////
    // Read Cluster non-member config
    ////
    void readTcmClusterParameters(CdbSession cdbSession)
        {
            try {
                ////
                // Read non-member configuration items
                ////
                String clusterPath = "/hcc:ha";
                Cluster.setClusterName(cdbSession.getElem(clusterPath
                                                          + "/token"));

                if (cdbSession.exists(clusterPath +
                                      "/local-user")) {
                    ConfBuf authUser = (ConfBuf)
                        cdbSession.getElem(clusterPath
                                           + "/local-user");
                    Cluster.setAuthUser(authUser.toString());
                }

                Cluster.setInterval((ConfInt32)
                                    cdbSession.getElem(clusterPath +
                                                       "/interval"));

                Cluster.setFailureLimit((ConfInt32)
                                        cdbSession.getElem(clusterPath +
                                                           "/failure-limit"));

                if (cdbSession.exists(clusterPath +
                                      "/bgp")) {
                    Cluster.setBgpConfig(true,
                                         (ConfInt32)
                                         cdbSession.getElem(clusterPath +
                                                            "/bgp/failure-limit"),
                                         (ConfIPPrefix)
                                         cdbSession.getElem(clusterPath +
                                                            "/bgp/anycast-prefix"),
                                         (ConfInt32)
                                         cdbSession.getElem(clusterPath +
                                                            "/bgp/anycast-path-min"),
                                         (ConfBool)
                                         cdbSession.getElem(clusterPath +
                                                            "/bgp/clear-enabled"));
                }
            }
            catch (Exception e) {
                log.error(" Could not read member values ", e);
            }
        }

    ////
    // Read Cluster member config
    ////
    void readTcmClusterMemCfg (CdbSession cdbSession, ConfKey key)
        {
            try {
                ConfPath memPath =
                    new ConfPath ("/hcc:ha/member{%x}", key);

                cdbSession.cd(memPath);

                // Delete it if its there and re-add
                Cluster.deleteMember(key.toString());

                ConfBuf name = (ConfBuf)cdbSession.getElem("name");
                ConfValue address = cdbSession.getElem("address");
                ConfEnumeration defaultHaRole =
                    (ConfEnumeration)cdbSession.getElem("default-ha-role");
                ConfBuf relay = new ConfBuf("none");
                if (cdbSession.exists("relay-name")) {
                    relay = (ConfBuf)cdbSession.getElem("relay-name");
                }
                ConfBool failoverMaster =
                    (ConfBool)cdbSession.getElem("failover-master");
                ConfBool clusterManager =
                    (ConfBool)cdbSession.getElem("cluster-manager");

                ConfList clusterNodes = null;
                if (cdbSession.exists("managed-cluster-nodes") == true) {
                    clusterNodes =
                        (ConfList)cdbSession.getElem("managed-cluster-nodes");
                }
                // Is the bgp device set?
                ConfBuf quaggaDevice = null;
                if (cdbSession.exists("quagga-device") == true) {
                    quaggaDevice = (ConfBuf)cdbSession.getElem("quagga-device");
                }

                // Add the newly added Cluster member
                Cluster.addMember(
                    new ClusterMember(key,
                                      name,
                                      address,
                                      defaultHaRole,
                                      relay,
                                      failoverMaster,
                                      clusterManager,
                                      clusterNodes,
                                      quaggaDevice));
            }
            catch (Exception e) {
                log.error(" Could not read member values ", e);
            }
        }

    ////
    // Obtain initial configuration
    ////
    void readTcmCfg (CdbSession cdbSession)
        {
            Logger log =
                Logger.getLogger(DiffIterateTcm.class);

            try {
                readTcmClusterParameters(cdbSession);
                ////
                // Read the cluster members defined
                ////
                int members = cdbSession
                    .getNumberOfInstances( "/hcc:ha/member");

                log.info ( " readTcmCfg: [" + members+
                           "] cluster members found" );
                for (int i=0; i < members; i++) {

                    String memPath = "/hcc:ha/member[%i]";
                    ConfBuf memName =
                        (ConfBuf)cdbSession.getElem(memPath + "/name", i);
                    ConfKey key = new ConfKey(memName);
                    ConfValue addr = cdbSession.getElem(
                        memPath + "/address", i);
                    ConfEnumeration defaultHaRole =
                        (ConfEnumeration)cdbSession.getElem(memPath +
                                                            "/default-ha-role", i);
                    ConfBuf relay = new ConfBuf("none");
                    if (cdbSession.exists(memPath + "/relay-name", i)) {
                        relay = (ConfBuf)cdbSession.getElem(memPath +
                                                            "/relay-name", i);
                    }
                    ConfBool failoverMaster =
                        (ConfBool)cdbSession.getElem(memPath +
                                                     "/failover-master",i);
                    ConfBool clusterManager =
                        (ConfBool)cdbSession.getElem(memPath +
                                                     "/cluster-manager",i);

                    // Is the cluster device nodes set?
                    ConfList clusterNodes = null;
                    if (cdbSession.
                        exists(memPath + "/managed-cluster-nodes",i) == true) {
                        clusterNodes =
                            (ConfList)cdbSession.
                            getElem(memPath + "/managed-cluster-nodes",i);
                    }
                    // Is the bgp device set?
                    ConfBuf quaggaDevice = null;
                    if (cdbSession.exists(memPath + "/quagga-device",i) == true) {
                        quaggaDevice =
                            (ConfBuf)cdbSession.getElem(memPath +
                                                        "/quagga-device",i);
                    }

                    Cluster.addMember(new ClusterMember(key,
                                                        memName,
                                                        addr,
                                                        defaultHaRole,
                                                        relay,
                                                        failoverMaster,
                                                        clusterManager,
                                                        clusterNodes,
                                                        quaggaDevice));
                }
            }
            catch ( Exception e ) {
                log.error("Could not start new session ",
                          e);
                throw new RuntimeException("Failed reading configuration", e);
            }
        }

    ////
    // Initialization method
    ////
    public void init() {

        log.info("  tcmApp: init CDB subscriber ");
        try {
            cdbSubscription =
                cdbSubSocket.newSubscription ();

            String path =  "/hcc:ha/";

            cdbSubscription
                .subscribe(1, tailfHcc.hash, path );

            cdbSubscription.subscribeDone();
            shouldRun = true;
        } catch ( Exception e ) {
            throw new RuntimeException("Fail in init", e);
        }
    }

    ////
    // App Finish method
    ////
    public void finish() {

        // Cancel the period timer thread
        timer.cancel();

        shouldRun = false;

        try {
            ResourceManager.unregisterResources(this);
        } catch ( Exception e ) {
            log.error("Finish Error",e);
        }
        finally {
        }
    }

    ////
    //  slave table periodically
    ////
    public class  PeriodicTask extends TimerTask {
        public void run() {

            try {
                Cluster.update();
            }
            catch (Exception e) {
                throw new RuntimeException("Failure in PeriodicTask", e);
            }
        }
    }
    public void setTimer (){

        // Cancel the period timer thread
        timer = new Timer();

        //Periodic timer
        timer.schedule(new PeriodicTask(), 0,
                       (Cluster.getInterval().intValue())*1000);
    }

    public void resetTimer (){

        // Cancel the period timer thread
        timer.cancel();
        timer = new Timer();

        //Periodic timer
        timer.schedule(new PeriodicTask(), 0,
                       (Cluster.getInterval().intValue())*1000);
    }
    public void run() {
        ////
        // Obtain the initial configuration and then
        // register for changes
        ////
        CdbSession cdbSession = null;
        try {
            EnumSet<CdbLockType> flags =
                EnumSet.<CdbLockType>of(CdbLockType.LOCK_REQUEST,
                                        CdbLockType.LOCK_PARTIAL);

            cdbSession =
                cdbDataSocket.startSession(CdbDBType.CDB_RUNNING, flags);

            if (cdbSession.exists("/hcc:ha")) {
                readInitTcmCfg(cdbSession);
            }
        }
        catch ( Exception e ) {
            log.error("Could not start new session ",
                      e);
            throw new RuntimeException("Failed reading configuration", e);
        }
        finally {
            if (cdbSession != null) {
                try {
                    cdbSession.endSession();
                    cdbDataSocket.close();
                }
                catch (Exception e) {
                    log.error("Could not end session ", e);
                }
            }
        }

        ////
        // Main loop for processing configuration changes
        ////

        while ( shouldRun ) {
            cdbSession = null;
            try {
                int[] point = cdbSubscription.read();

                EnumSet<CdbLockType> flags =
                	     EnumSet.<CdbLockType>of(CdbLockType.LOCK_REQUEST,
                                 CdbLockType.LOCK_WAIT);

                cdbSession =
                    cdbDataSocket.startSession(CdbDBType.CDB_RUNNING, flags);

                cdbSubscription
                    .diffIterate(point[0],
                                 new DiffIterateTcm(),
                                 EnumSet.of(DiffIterateFlags
                                            .ITER_WANT_PREV)
                                 , cdbSession);

                cdbSession.endSession();
            }
            catch (IOException e) {
                log.warn("IO Error - probably caused by a package reload");
            }
            catch (IllegalStateException e) {
                log.warn("IO Error - probably caused by a package reload");
            }
            catch (Exception e) {
                log.error("", e);
            }
            finally {
                try {
                    cdbSubscription.sync(CdbSubscriptionSyncType.
                                         DONE_PRIORITY);
                } catch ( ConfException e ) {
                    log.fatal("Could not sync subscriber system may hang! "
                              ,e );
                } catch ( IOException e ) {
                    log.warn("IO Error - probably caused by a package reload");
                }
                catch (IllegalStateException e) {
                    log.warn("IO Error - probably caused by a package reload");
                }
                if (cdbSession != null) {
                    try {
                        cdbSession.endSession();
                    }
                    catch (Exception e) {
                        log.error("Could not end session ", e);
                    }
                }
            }
        }
    }

    private class ModifyActivation implements Runnable {
        public void run() {
            //make sure we clear cluster status, and
            //set BGP to none
            Cluster.modifyActivation(false, false);
        }
    }
    private class Deactivate implements Runnable {
        public void run() {
            Cluster.deactivate();
            Cluster.reset();
        }
    }
    private class ActivateBGP implements Runnable {
        public void run() {
            if (Cluster.getActivatedValue()) {
                Cluster.activateBGP();
            } else {
                Cluster.deactivateBGP();
            }
        }
    }
    private class DeactivateBGP implements Runnable {
        public void run() {
            Cluster.deactivateBGP();
        }
    }
}
