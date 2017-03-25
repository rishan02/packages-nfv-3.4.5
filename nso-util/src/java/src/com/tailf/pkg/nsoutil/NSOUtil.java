package com.tailf.pkg.nsoutil;

import java.io.IOException;
import java.net.Socket;
import org.apache.log4j.Logger;

import com.tailf.conf.*;
import com.tailf.maapi.*;
import com.tailf.ncs.*;

public class NSOUtil {
    private static final Logger LOGGER = Logger.getLogger(NSOUtil.class);
    /**
     * Pre-requirement: HA mode is enabled.
     */
    public static boolean isMaster(Maapi maapi, int tid)

        throws ConfException, IOException {

        ConfEnumeration ha_mode_enum =  (ConfEnumeration)
            maapi.getElem(tid, "/tfnm:ncs-state/ha/mode");

        String ha_mode =
            ConfEnumeration.getLabelByEnum(
                   "/tfnm:ncs-state/ha/mode",
                   ha_mode_enum);

        if (!("none".equals(ha_mode) ||
              "normal".equals(ha_mode) ||
              "master".equals(ha_mode))) {
            // slave or relay-slave
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isHaEnabled(Maapi maapi, int tid)
        throws ConfException, IOException {

        return maapi.exists(tid, "/tfnm:ncs-state/ha");
    }


    /**
     * Redeploy the service pointed to by the string <code>path</code>.
     * The reactive-re-deploy action will be used if it is available.
     * The redeploy takes place in a new thread making this method safe
     * to call from a CDB subscriber.
     * The redeploy action will be called using the user <code>
     * "admin"</code> and the context <code>"system"</code>.
     *
     * @param path Path to the service to redeploy.
     */
    public static void redeploy(String path) {
        redeploy(path, "admin");
    }

    /**
     * Redeploy the service pointed to by the string <code>path</code>.
     * The reactive-re-deploy action will be used if it is available.
     * The redeploy takes place in a new thread making this method safe
     * to call from a CDB subscriber.
     * The redeploy action will be called using the specified user and
     * the context <code>"system"</code>.
     *
     * @param path Path to the service to redeploy.
     * @param user Name of the user used for the redeploy session.
     */

    public static void redeploy(String path, String user) {
        Redeployer r = new Redeployer(path, user);
        Thread t = new Thread(r);
        t.start();
    }

    private static class Redeployer implements Runnable {
        private String path;
        private String user;
        private Maapi redepMaapi;
        private Socket redepSock;

        public Redeployer(String path, String user) {
            this.path = path;
            this.user = user;
            try {
                redepSock = new Socket(NcsMain.getInstance().getNcsHost(),
                                       NcsMain.getInstance().getNcsPort());
                redepMaapi = new Maapi(redepSock);
                redepMaapi.startUserSession(this.user,
                                            redepMaapi.getSocket().getInetAddress(),
                                            "system",
                                            new String[] {},
                                            MaapiUserSessionFlag.PROTO_TCP);
            } catch (Exception e) {
                LOGGER.error(String.format("redeployer exception: %s", e));
            }
        }

        /*
         * Suppress warning of checking the version of Conf.LIBVSN,
         * since this will depend on which version of NCS is used.
         */
        @SuppressWarnings("unused")
        public void run() {
            try {
                /* Must be different, we want to redeploy owner if he exists. */
                int tid = redepMaapi.startTrans(Conf.DB_RUNNING, Conf.MODE_READ);
                LOGGER.debug(String.format("invoking redeploy on %s", path));

                if (Conf.LIBVSN >= 0x06010000) {
                    /*
                     * For NSO 4.1 and later the reactive-re-deploy
                     * is introduced and should be used
                     */
                    redepMaapi.requestAction(new ConfXMLParam[] {},
                                             String.format("%s/reactive-re-deploy",path));
                } else {
                    redepMaapi.requestAction(new ConfXMLParam[] {},
                                             String.format("%s/re-deploy",path));
                }

                try {
                    redepMaapi.finishTrans(tid);
                } catch (Throwable ignore) {
                    ;
                }
                redepSock.close();
            } catch (Exception e) {
                LOGGER.error(String.format("error in re-deploy: %s %s", path, e));
                return;
            }
        }
    }
}
