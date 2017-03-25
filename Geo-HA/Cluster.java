package com.tailf.ns.tailfHcc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.tailf.conf.Conf;
import com.tailf.conf.ConfBool;
import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfEnumeration;
import com.tailf.conf.ConfHaNode;
import com.tailf.conf.ConfIP;
import com.tailf.conf.ConfIPPrefix;
import com.tailf.conf.ConfIPv4;
import com.tailf.conf.ConfIPv6;
import com.tailf.conf.ConfInt32;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfList;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfValue;
import com.tailf.conf.ConfXMLParam;
import com.tailf.ha.Ha;
import com.tailf.ha.HaException;
import com.tailf.ha.HaStateType;
import com.tailf.ha.HaStatus;
import com.tailf.maapi.Maapi;
import com.tailf.maapi.MaapiUserSessionFlag;
import com.tailf.navu.NavuAction;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.ncs.NcsMain;
import com.tailf.ncs.ns.Ncs;

public class Cluster {

    private static Logger LOGGER = Logger.getLogger(Cluster.class);

    /////
    // Temporary active and override values
    ////
    private static boolean activated = false;
    private static boolean overrideActive = false;
    private static ConfEnumeration override;
    private static boolean nodeTransitioned = false;
    private static boolean failover = false;
    private static int roleFailures = 0;
    private static boolean slaveProgess = false;

    private static boolean forcedBeSlaveTo = false;
    private static String forcedMaster;

    private enum HaConState {INIT, CONNECTED, DISCONNECTED};
    private static HaConState connectState = HaConState.INIT ;

    ////
    // HA Socket info
    ////
    private static Socket socket;
    private static Ha h;

    ////
    // Cluster configuration parameters
    ////
    private static ConfValue clusterName;
    private static String authUser;
    private static String localNode;
    private static ConfInt32 interval;
    private static ConfInt32 failureLimit;
    private static boolean bgpAnycastEnabled = false;
    private static ConfInt32 bgpFailureLimit;
    private static int bgpAnycastFailures = 0;
    private static ConfIPPrefix bgpAnycastPrefix;
    private static ConfInt32 bgpAnycastMin;
    private static ConfBool bgpClearEnabled;
    ////
    // Number of active slave for this node
    ////
    private static int numInstances;

    ////
    //Array list to store active slave objects
    ////
    private static ArrayList<Object> members;

    ////
    //Alarm message
    ////
    private static String alarmReason;

    ////
    // Initalize the active slave nodes table
    ////
    public static void init() throws UnknownHostException {

        try {
            // Save local host name
            localNode = java.net.InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
        }

        // Initalize list to hold cluster member nodes
        members = new ArrayList<Object>();
    }

    public synchronized static boolean getActivatedValue() {
        return activated;
    }

    public synchronized static void setClusterName(ConfValue name) {
        clusterName = name;
    }

    public synchronized static void setAuthUser(String name) {
        authUser = name;
    }

    public synchronized static void setInterval(ConfInt32 i) {
        interval = i;
    }
    public synchronized static ConfInt32 getInterval() {
        return interval;
    }

    public synchronized static void setFailureLimit(ConfInt32 i) {
        failureLimit = i;
    }

    public synchronized static void setBgpAnycastEnabled(boolean v) {
        bgpAnycastEnabled = v;
    }

    public synchronized static void setBgpConfig(boolean v,
                                                 ConfInt32 failureLimit,
                                                 ConfIPPrefix prefix,
                                                 ConfInt32 min,
                                                 ConfBool clear) {
        bgpAnycastEnabled = v;
        bgpFailureLimit = failureLimit;
        bgpAnycastPrefix = prefix;
        bgpAnycastMin = min;
        bgpClearEnabled = clear;
    }

    public synchronized static int getNumInstances() {
        return numInstances;
    }

    ////
    // Find a master node in the table
    ////
    private synchronized static ClusterMember
        findMaster() {

        Iterator<Object> it = members.iterator();
        while (it.hasNext()) {
            ClusterMember mem = (ClusterMember)it.next();
            if (HaStateType.valueOf(mem.defaultHaRole.getOrdinalValue())
                == HaStateType.MASTER)
                return (mem);
        }
        // Not present
        return (null);
    }
    private synchronized static ClusterMember
    findSlave() {

    Iterator<Object> it = members.iterator();
    while (it.hasNext()) {
        ClusterMember mem = (ClusterMember)it.next();
        if (HaStateType.valueOf(mem.defaultHaRole.getOrdinalValue())
            == HaStateType.SLAVE)
            return (mem);
    }
    // Not present
    return (null);
}
    ////
    // Find the first transitioned enabled node in
    // the cluster member
    ////
    private synchronized static ClusterMember
        findFailoverMaster() {

        Iterator<Object> it = members.iterator();
        while (it.hasNext()) {
            ClusterMember mem = (ClusterMember)it.next();
            if (mem.failoverMaster.booleanValue())
                return (mem);
        }
        // Not present
        return (null);

    }

    private synchronized static ClusterMember
        findForcedMaster() {
        return findMemberByName(forcedMaster);
    }

    ////
    // Find a specific node in the table by name
    ////
    public synchronized static ClusterMember
        findMemberByName(String name) {

        Iterator<Object> it = members.iterator();
        while (it.hasNext()) {

            ClusterMember mem = (ClusterMember)it.next();
            if (name.equals(mem.name.toString())) {
                return (mem);
            }
        }
        LOGGER.debug("findMemberByName: '"+name+
                     "' did not match any ha member name." +
                     "The ha member name must match the machine hostname");
        // Not present
        return (null);
    }

    ////
    // Add a member to the cluster
    ////
    public synchronized static void addMember(ClusterMember mem) {
        numInstances++;
        members.add(mem);
    }

    ////
    // Delete a member from the cluster
    ////
    public synchronized static void deleteMember(String name) {

        Iterator<Object> it = members.iterator();
        while (it.hasNext()) {
            ClusterMember mem = (ClusterMember)it.next();
            if (name.equals(mem.key.toString())) {
                numInstances--;
                it.remove();
            }
        }
    }
    public synchronized static String getDevice(ClusterMember mem) {
        if (mem.quaggaDevice == null)
            return (null);

        return (mem.quaggaDevice.toString());
    }

    public synchronized static String getRole(ClusterMember mem) {

        if (mem == null)
            return (null);

        HaStateType role =
            HaStateType.valueOf(mem.defaultHaRole.getOrdinalValue());

        return (role.toString());
    }

    public synchronized static ConfList getDeviceNodes(ClusterMember mem) {

        if (mem == null)
            return (null);
        return mem.clusterNodes;
    }

    ////
    // Iterator method
    ////
    public synchronized static Iterator<Object> iterator() {
        return members.iterator();
    }

    ////
    // Activate and de-activate the HA node
    ////
    public synchronized static void activate() {

        modifyActivation(true, true);
    }
    public synchronized static void deactivate() {

        modifyActivation(false, true);
    }
    public synchronized static void modifyActivation(boolean act,
                                                     boolean update) {
        ////
        // Go to NONE first
        ////
        try {
            if (update) {
                activated = act;
            }

            h.beNone();
            LOGGER.debug("After beNone, ModifyActivation: ");
            LOGGER.debug("update: " + update + " with activation:" + act);

            // Is bgp anycast support enabled
            if (act) {
                activateBGP();
            }
            else {
                deactivateBGP();
            }
        }
        catch (Exception e) {
            LOGGER.error("RoleOverride error: ", e);
        }
    }

    public synchronized static void activateBGP() {
        // Find the local node
        ClusterMember myNode = findMemberByName(localNode);
        if (overrideActive == true) {
            String role =
                HaStateType.valueOf(override.getOrdinalValue()).toString();
            BgpAnycast.applyConfig(myNode,
                                   role,
                                   authUser);
        }
        else {
            BgpAnycast.applyConfig(myNode,
                                   authUser);
        }
    }

    public synchronized static void deactivateBGP() {
        // Find the local node
        ClusterMember myNode = findMemberByName(localNode);
        BgpAnycast.applyConfig(myNode,
                               "none",
                               authUser);
    }

    public static boolean applyConfig(ClusterMember myNode) {

        if ((myNode != null) &&
            bgpAnycastEnabled &&
            (myNode.quaggaDevice != null))
            return (true);

        return(false);

    }
    public static boolean applyClearBGPSessions(ClusterMember myNode) {

        if ((applyConfig(myNode) == true) &&
            (bgpClearEnabled.booleanValue() == true))
            return (true);

        return(false);

    }
    public static void roleOverride(ConfEnumeration ovr) {
        try {

            h.beNone();

            HaStateType role =
                HaStateType.valueOf(ovr.getOrdinalValue());

            String roleStr;
            if (failover) {
                roleStr = "failover-master";
                failover = false;
            }
            else {
                roleStr = role.toString();
            }

            LOGGER.debug("After beNone, RoleOverride: " + roleStr);

            // Reset connection state
            connectState = HaConState.INIT;
            nodeTransitioned = false;

            // Override is active
            overrideActive = true;

            override = ovr;

            // Find the local node
            ClusterMember myNode = findMemberByName(localNode);

            // Apply Config changes
            if (applyConfig(myNode)) {
            	
                BgpAnycast.applyConfig(myNode, roleStr, authUser);
            }

        }
        catch (Exception e) {
            LOGGER.error("RoleOverride: error:", e);
        }
    }

    public static void beSlaveTo(ConfBuf newMaster) {
        try {

            LOGGER.trace("beSlaveTo: trying to be slave to: " +
                         newMaster.toString());
            h.beNone();

            // Reset connection state
            connectState = HaConState.INIT;
            nodeTransitioned = false;

            // Override is active
            forcedBeSlaveTo = true;
            forcedMaster = newMaster.toString();


            // Find the local node
            ClusterMember myNode = findMemberByName(localNode);

            // Apply Config changes
            if (applyConfig(myNode)) {
                BgpAnycast.applyConfig(myNode, "slave", authUser);
            }
        }
        catch (Exception e) {
            LOGGER.error("beSlaveTo: error:", e);
        }
    }

    public synchronized static void roleRevert(String user,
                                               String context) {

        try {

            h.beNone();

            LOGGER.debug("After beNone, RoleRevert");

            // Override is active
            overrideActive = false;
            nodeTransitioned = false;

            // Reset failure(s)
            roleFailures = 0;
            bgpAnycastFailures = 0;
            // Reset connection state
            connectState = HaConState.INIT;

            // Find the local node
            ClusterMember myNode = findMemberByName(localNode);
            ClusterMember slave = findSlave();
            HccAlarms hccAlarms = HccAlarms.getHccAlarms();
            if (getRole(myNode).equals("MASTER")) {
                hccAlarms.setMasterNode(localNode);
            }
            hccAlarms.clearAllAlarms(user, context);

            // Is bgp anycast support enabled
            BgpAnycast.applyConfig(myNode,
                                   authUser);
            

            // Is bgp anycast support enabled
            BgpAnycast.applyConfig(slave,
                                   authUser);

        }
        catch (Exception e) {
            LOGGER.error("RoleRevert: error:", e);
        }
    }

    public synchronized static String getStatusDetails() {

        String status = "none";
        String rVal = null;
        try {
            String node = "master";
            String local =
                java.net.InetAddress.getLocalHost().getHostName();

            HaStatus cstat = h.status();
            ConfHaNode[] nodes = cstat.getNodes();
            status = cstat.getHaState().toString().toLowerCase();

            if (status.equals("master"))
                node = "slave";

            if (nodes != null) {
                for (ConfHaNode curr : nodes) {
                    rVal = local + "["
                        + status + "] "
                        + "connected "
                        + curr.getNodeId().toString()
                        + "[" + node + "]";
                }
            }
            else {
                rVal = local + "[" + status + "]";
            }

        }
        catch (Exception e) {
            LOGGER.error("GetStatus: error:", e);
        }
        return (rVal);
    }

    ////
    // Get Cluster Device Slave Node connection state.
    ////
    public synchronized static String getConnectionState() {

        String rVal = null;

        try {

            String local =
                java.net.InetAddress.getLocalHost().getHostName();

            // Find the local node member config
            ClusterMember n = findMemberByName(local);

            // If NCS Cluster Service Node or HA Master return Not applicable...
            if ((n.clusterManager.booleanValue()) ||
                (HaStateType.valueOf(n.defaultHaRole.getOrdinalValue())
                 != HaStateType.SLAVE)) {
                rVal = "na";
            }
            else {
                switch (connectState) {
                case INIT:
                case CONNECTED:
                    rVal = "ok";
                    break;
                case DISCONNECTED:
                    rVal = "disconnected";
                    break;
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("GetConnectionState: error:", e);
        }

        return (rVal);
    }

    private static HaStateType
        checkBgpTransition(ClusterMember n,
                           HaStateType currentStatus,
                           HaStateType role) throws Exception {

        ////
        // Don't bother if BGP isn't enabled
        ////
        if (!bgpAnycastEnabled)
            return(role);

        ////
        // Only SLAVE non-overridden nodes transition
        ////
        if ((overrideActive) || (role != HaStateType.SLAVE))
            return (role);

        ////
        // Check the path count query the local BGP instance
        ////

        String prefix = bgpAnycastPrefix.toString();

        if ((prefix.equals("0.0.0.0/0")) ||
            (prefix.equals("::/0"))) {
            ////
            // Default is not valid don't use it
            ////
            return (role);

        }

        int paths = BgpAnycast.queryPrefix(getDevice(n),
                                           prefix,
                                           authUser);

        if (paths < bgpAnycastMin.intValue())  {

            bgpAnycastFailures++;

            if (bgpAnycastFailures >= bgpFailureLimit.intValue()) {
                ////
                // Generate a notification
                ////
                if (bgpAnycastFailures == bgpFailureLimit.intValue()) {

                    HccAlarms hccAlarms = HccAlarms.getHccAlarms();
                    if (!hccAlarms.isRaisedAlarm()) {
                        // Search for the relay node
                        ClusterMember myNode = findMemberByName(localNode);
                        ClusterMember master =
                            findMemberByName(myNode.relayName.toString());

                        // No relay is configured
                        if (master == null)
                            master = findMaster();
                        // Get the connection state of all Device
                        // instance nodes
                        alarmReason =
                            "Anycast Prefix Path(s) lost. '" + localNode +
                            "' transitioning to HA MASTER role. " +
                            "When the problem has been fixed, "+
                            "role-override the old MASTER to SLAVE to "+
                            "prevent config loss, then role-revert all nodes. "+
                            "This will clear the alarm.";

                        hccAlarms.setMasterNode(master.name.toString());
                        hccAlarms.setRaisedAlarm();
                        hccAlarms.setAlarmType("bgp");
                        hccAlarms.setReason(alarmReason);
                    }

                    // Log it the first time only
                    LOGGER.info ("Anycast Prefix Path(s) lost " +
                                 "transitioning to HA MASTER role");
                    ////
                    // If this node is the top node in the NCS cluster
                    // it can modify all cluster member roles
                    ////
                    if (n.clusterManager.booleanValue()) {
                        clusterWideAction("role-override",
                                          "<role>master</role>",
                                          authUser,
                                          "system");
                        ConfPath path =
                            new ConfPath("/hcc:ha/member{%x}/default-ha-role",
                                         n.name);
                        ConfEnumeration _role =
                            ConfEnumeration.getEnumByLabel(path, "master");
                        failover = true;

                        roleOverride(_role);

                    }
                }
                // Make sure Slave can transition...
                if (n.failoverMaster.booleanValue()) {
                    ////
                    // Transition this node to master
                    ////
                    nodeTransitioned = true;
                    return(HaStateType.MASTER);
                }
            }
        }
        else {
            ////
            // Path(s) have been restored
            ////
            if (bgpAnycastFailures < bgpFailureLimit.intValue())
                bgpAnycastFailures = 0;
        }

        /////
        // If the path(s) have been restored but the node has
        // already transitioned hold the role. No AUTO-REVERT
        /////
        if (bgpAnycastFailures > failureLimit.intValue()) {
            return(HaStateType.MASTER);
        }

        return(role);
    }

    private static HaStateType
        checkTransition(ClusterMember n,
                        HaStateType currentStatus,
                        HaStateType role) throws Exception {

        ////
        // Only SLAVE non-overridden nodes transition
        ////
        if ((overrideActive) || (role != HaStateType.SLAVE))
            return (role);

        if (currentStatus != role) {

            ////
            // Count the role failure
            ////
            roleFailures++;

            if (roleFailures >= failureLimit.intValue()) {

                ////
                // Generate a notification
                ////
                if (roleFailures == failureLimit.intValue()) {

                    HccAlarms hccAlarms = HccAlarms.getHccAlarms();
                    if (!hccAlarms.isRaisedAlarm()) {
                        // Search for the relay node
                        ClusterMember myNode = findMemberByName(localNode);
                        ClusterMember master =
                            findMemberByName(myNode.relayName.toString());

                        // No relay is configured
                        if (master == null)
                            master = findMaster();
                        // Get the connection state of all Device instance nodes

                        alarmReason =
                            "HA connection lost. '" + localNode +
                            "' transitioning to HA MASTER role. " +
                            "When the problem has been fixed, "+
                            "role-override the old MASTER to SLAVE to "+
                            "prevent config loss, then role-revert all nodes. "+
                            "This will clear the alarm.";

                        hccAlarms.setMasterNode(master.name.toString());
                        hccAlarms.setRaisedAlarm();
                        hccAlarms.setAlarmType("service");
                        hccAlarms.setReason(alarmReason);
                    }

                    // Note connection has been lost
                    if (connectState == HaConState.CONNECTED) {
                        connectState = HaConState.DISCONNECTED;
                    }
                    ////
                    //If Slave node not enabled to transition, log
                    //   and return here.
                    ////
                    if (!n.failoverMaster.booleanValue()) {
                        LOGGER.info ("Failure Count Exceeded " +
                                     "Non-transition enabled node");
                        return(role);
                    }

                    ////
                    // If this node is the top node in the NCS cluster
                    // it can modify all cluster member roles
                    ////
                    if (n.clusterManager.booleanValue()) {
                        LOGGER.info ("Failure Count Exceeded Node " +
                                     "transitioning to HA MASTER role");
                        clusterWideAction("role-override",
                                          "<role>master</role>",
                                          authUser,
                                          "system");
                        ConfPath path =
                            new ConfPath("/hcc:ha/member{%x}/default-ha-role",
                                         n.name);
                        ConfEnumeration _role =
                            ConfEnumeration.getEnumByLabel(path, "master");

                        failover = true;

                        roleOverride(_role);
                    }
                }

                // Make sure Slave can transition...
                if (n.failoverMaster.booleanValue()) {
                    ////
                    // Transition this node to master
                    ////
                    nodeTransitioned = true;
                    return(HaStateType.MASTER);
                }
            }
        }
        else {
            // Connectivity restored
            roleFailures = 0;
            connectState = HaConState.CONNECTED;
        }
        return (role);
    }

    ////
    // Check if HA Slave NCS Cluster Device nodes have disconnected
    // from HA Master.
    //  If device nodes indicate disconnect, fail entire NCS Cluster to Master.
    ///
    private static HaStateType
        checkClusterConnectionState(ClusterMember n,
                                    HaStateType currentStatus,
                                    HaStateType role) throws Exception {
        String result = "";

        ////
        // If this node is not the Service node in the NCS Slave cluster, exit
        ////
        if (!n.clusterManager.booleanValue() || (role != HaStateType.SLAVE))
            return (role);

        // Get the connection state of all Device instance nodes
        result = clusterWideAction("connect-state",
                                   "",
                                   authUser,
                                   "system");

        // process result here... if "disconnected", initiate Cluster failover..
        if ((result != null) && (result.contains("disconnected"))) {

            HccAlarms hccAlarms = HccAlarms.getHccAlarms();
            if (!hccAlarms.isRaisedAlarm()) {
                ClusterMember myNode = findMemberByName(localNode);
                ClusterMember master =
                    findMemberByName(myNode.relayName.toString());

                // No relay is configured
                if (master == null)
                    master = findMaster();

                alarmReason =
                    "Cluster Device node HA connection lost. '" + localNode +
                    "' transitioning to HA MASTER role. " +
                    "When the problem has been fixed, "+
                    "role-override the old MASTER to SLAVE to "+
                    "prevent config loss, then role-revert all nodes. "+
                    "This will clear the alarm.";

                hccAlarms.setMasterNode(master.name.toString());
                hccAlarms.setRaisedAlarm();
                hccAlarms.setAlarmType("device");
                hccAlarms.setReason(alarmReason);
            }

            LOGGER.info("Cluster Device node HA connection lost..." +
                        " initiating failover");

            clusterWideAction("role-override",
                              "<role>master</role>",
                              authUser,
                              "system");

            ConfPath path =
                new ConfPath("/hcc:ha/member{%x}/default-ha-role",
                             n.name);
            ConfEnumeration _role =
                ConfEnumeration.getEnumByLabel(path, "master");
            failover = true;
            roleOverride(_role);

            // Transition this node to master too
            nodeTransitioned = true;
            return(HaStateType.MASTER);
        }

        return (role);
    }

    ////
    // Make sure the local node is in the appropriate state
    ////
    private static void
        updateLocalNode(Ha h, HaStatus status) throws Exception {

        ClusterMember myNode = findMemberByName(localNode);
        ClusterMember master = null;

        HaStateType currentStatus = status.getHaState();

        if (slaveProgess == true)
            return;

        // If the local node has been configured
        if (myNode != null) {

            HaStateType role;

            ////
            // If the override is active use the provided value
            ////
            if (overrideActive == true) {
                role =
                    HaStateType.valueOf(override.getOrdinalValue());
            }
            else if (nodeTransitioned == true) {
                role = currentStatus;
            }
            else if (forcedBeSlaveTo) {
                role = HaStateType.SLAVE;
            }
            else {
                role =
                    HaStateType.valueOf(myNode.defaultHaRole.getOrdinalValue());

                ////
                // Check if see if there a valid transition
                // needed. Only non-override configured slaves can
                // transition
                ////

                // Verify Slave <==> Master connectivity
                role = checkTransition(myNode, currentStatus, role);

                // Verify NCS Cluster Device node connectivity
                role = checkClusterConnectionState(myNode, currentStatus,
                                                   role);

                // Verify BGP Anycast connectivity
                role = checkBgpTransition(myNode, currentStatus, role);
            }

            if (currentStatus != role) {

                // Find the master node
                if (role == HaStateType.SLAVE) {

                    // Search for the relay node
                    master = findMemberByName(myNode.relayName.toString());

                    // No relay is configured
                    if (master == null)
                        master = findMaster();
                    // Get the connection state of all Device instance nodes

                    if (overrideActive == true) {
                        master = findFailoverMaster();
                    }

                    if (forcedBeSlaveTo) {
                        master = findForcedMaster();
                        forcedBeSlaveTo = false;
                    }
                }

                if (role == HaStateType.SLAVE_RELAY) {
                    master = findMaster();
                }

                try {
                    InetAddress a = null;
                    ConfIP ip = null;

                    switch (role) {

                    case MASTER:
                        a= InetAddress.getByName("localhost");

                        if (a.getAddress().length == 4)
                            ip = new ConfIPv4(a);
                        else
                            ip = new ConfIPv6(a.getHostAddress());

                        ConfHaNode node =
                            new ConfHaNode(
                                           new ConfBuf(localNode), ip);

                        LOGGER.debug("UpdateLocalNode - beMaster");
                        h.beMaster(node.getNodeId());

                        HccAlarms hccAlarms = HccAlarms.getHccAlarms();
                        hccAlarms.raiseAlarm();

                        if (myNode.inTransition != role) {
                            myNode.inTransition = role;
                        }
                        
                         myNode = findMemberByName(localNode);
                        // Is bgp anycast support enabled
                        BgpAnycast.applyConfig(myNode,"FAILOVER-MASTER",
                                               authUser);
                        
                        break;

                    case NONE:
                        LOGGER.debug("UpdateLocalNode - beNone");
                        h.beNone();

                        if (myNode.inTransition != role) {
                            myNode.inTransition = role;
                        }
                        break;

                    case SLAVE_RELAY:
                        ////
                        // Have to transition from slave to relay
                        ////
                        if (currentStatus == HaStateType.SLAVE) {
                            LOGGER.debug("UpdateLocalNode - beRelay");
                            h.beRelay();
                            break;
                        }

                    case SLAVE:
                        ////
                        // Become slave if possible
                        ////
                        if (master != null) {
                            //slaveProgess = true;
                            a = InetAddress.getByName(
                                                      master.address.toString());
                            if (a.getAddress().length == 4)
                                ip = new ConfIPv4(a);
                            else
                                ip = new ConfIPv6(a.getHostAddress());

                            ConfHaNode slave =
                                new ConfHaNode(new ConfBuf(master.name.toString()),
                                               ip);

                            if (myNode.inTransition != role) {
                                myNode.inTransition = role;
                            }

                            LOGGER.debug("UpdateLocalNode - beSlave");
                            h.beSlave(new ConfBuf(localNode), slave, true);

                            ////
                            // Existing HA bug reset the HA socket
                            ////
                            haReinitialize();
                        }
                        break;

                    default:
                        break;
                    }

                }
                catch (Exception e)
                    {
                        System.out.println("Exception");
                    }
            }
            else {
                myNode.inTransition = role;
            }
        }
    }

    ////
    // Intialize the HA Socket
    ////
    private static void haReinitialize() throws HaException {

        try {

            // Close existing socket
            socket.close();

            haInitialize();

        }
        catch (Exception e){
            throw new HaException("Failed to create HA connection", e);
        }

    }

    public static void haInitialize() throws HaException{

        try {
            LOGGER.debug("tailf-hcc service initializing.... ");
            int port = NcsMain.getInstance().getNcsPort();
            socket = new Socket("localhost", port);
            h = new Ha(socket, clusterName.toString());
        }
        catch (Exception e) {
            throw new HaException("NCS HA is not enabled", e);
        }
    }
    ////
    // Recover HA state upon java restart if possible
    ////
    public static Boolean haRestartRecovery() throws HaException {
        HaStatus status = null;

        ////
        // Read the current HA status
        ////
        try {

            LOGGER.info("HA Cluster Attempting re-start recovery.... ");
            status = h.status();

            // Lookup local node configuration
            ClusterMember myNode = findMemberByName(localNode);

            // Current node isn't in the cluster just return
            if (myNode == null)
                return(false);

            HaStateType currentStatus = status.getHaState();
            HaStateType configuredRole =
                HaStateType.valueOf(myNode.defaultHaRole.getOrdinalValue());

            switch (currentStatus) {

            case MASTER:
            case SLAVE_RELAY:
            case SLAVE:

                if (configuredRole == currentStatus) {
                    LOGGER.info("Current role is configured role");
                    activated = true;
                }
                else {
                    LOGGER.info("Override or Transition occurred resumming " +
                                "current role");
                    activated = true;
                    overrideActive = true;
                    override = new ConfEnumeration(currentStatus.getValue());

                }
                return (true);


            case NONE:
            default:
                LOGGER.info("tailf-hcc: No valid role exists Cluster or " +
                            "Node activation required !!");
                return(false);
            }


        }
        catch (Exception e) {
            throw new HaException("Failed to create HA connection", e);
        }
    }

    ////
    // Update the cluster table with most recent nodes
    ////
    public synchronized static void update()
        throws HaException, IOException {

        HaStatus status = null;

        /////
        // Has this node been activated
        ////
        if (activated == false) {
            return;
        }

        ////
        // Start up HA connection
        ////
        try {

            status = h.status();

            ////
            // Take the appropriate action for the local node
            ////
            updateLocalNode(h, status);

        }
        catch (Exception e) {
            throw new HaException("Failed to create HA connection", e);
        }
    }



    ////
    // Exuecute cluster-wide actions
    ////
    public static String clusterWideAction(String cmd,
                                           String xml,
                                           String user,
                                           String context) {

        String Result = "";
        Socket socket = null;
        try {
            ClusterMember myNode = findMemberByName(localNode);
            ConfList deviceNodes = getDeviceNodes(myNode);

            // Is /ha/member/managed-cluster-nodes
            // configured...
            if (deviceNodes != null) {
                 socket = new Socket("127.0.0.1", Conf.NCS_PORT);
                Maapi maapi = new Maapi(socket);

                maapi.startUserSession(user,
                                       InetAddress.getLocalHost(),
                                       context,
                                       new String[] { user },
                                       MaapiUserSessionFlag.PROTO_TCP);

                int th = maapi.startTrans(Conf.DB_RUNNING,Conf.MODE_READ);

                NavuContext ctx = new NavuContext(maapi, th);

                for(ConfObject deviceNode: deviceNodes.elements()) {
                    NavuAction action = new NavuContainer(ctx).
                        container(new Ncs().hash()).
                        container(Ncs._cluster).
                        list(Ncs._remote_node).
                        elem(new ConfKey((ConfValue)deviceNode)).
                        container("hcc","commands").
                        action(cmd);

                    ConfXMLParam[] result = action.call(xml);
                    Result = Result + "\n" + result[0].getValue().toString();
                }
                maapi.finishTrans(th);
                socket.close();
            }
            else {
                Result = "cluster-nodes not configured";
            }
        }
        catch (Exception e) {
            LOGGER.info("Cluster command: Failed to connect to remote device.",
                        e);
            try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            return(null);
        }

        return (Result);
    }

    public static void readOnly(ConfBool mode, String user, String context) {

        try {
            Socket socket = new Socket("127.0.0.1", Conf.NCS_PORT);
            Maapi maapi = new Maapi(socket);

            maapi.startUserSession(user,
                                   InetAddress.getLocalHost(),
                                   context,
                                   new String[] { user },
                                   MaapiUserSessionFlag.PROTO_TCP);
            maapi.setReadOnlyMode(mode.booleanValue());
            socket.close();
        }
        catch (Exception e) {
            LOGGER.error("ReadOnly: error:", e);
        }
    }

    public synchronized static void reActivate(String user, String context) {
        ////
        // Go to NONE first
        ////
        try {
            activated = true;

            roleRevert(user, context);

            // Find the local node
            ClusterMember myNode = findMemberByName(localNode);
            // Is bgp anycast support enabled
            BgpAnycast.applyConfig(myNode,
                                   authUser);
        }
        catch (Exception e) {
            LOGGER.error("ReActivation error: ", e);
        }
    }
    public synchronized static void reset() {
        LOGGER.info("tailf-hcc Reset node called...");
        activated = false;
        overrideActive = false;
        override = null;
        nodeTransitioned = false;
        failover = false;
        roleFailures = 0;
        slaveProgess = false;
        connectState = HaConState.INIT ;

        clusterName = null;
        authUser = null;
        localNode = null;
        interval = null;
        failureLimit = null;
        bgpAnycastEnabled = false;
        bgpFailureLimit = null;
        bgpAnycastFailures = 0;
        bgpAnycastPrefix = null;
        bgpAnycastMin = null;
        bgpClearEnabled = null;
        numInstances = 0;

        members = null;
        alarmReason = null;
    }
}
