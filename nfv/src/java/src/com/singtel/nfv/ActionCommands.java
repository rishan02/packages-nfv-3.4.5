package com.singtel.nfv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.singtel.nfv.namespaces.nfv;
import com.singtel.nfv.namespaces.nfvTypes;
import com.tailf.conf.Conf;
import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfEnumeration;
import com.tailf.conf.ConfException;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfTag;
import com.tailf.conf.ConfXMLParam;
import com.tailf.conf.ConfXMLParamValue;
import com.tailf.dp.DpActionTrans;
import com.tailf.dp.DpCallbackException;
import com.tailf.dp.annotations.ActionCallback;
import com.tailf.dp.proto.ActionCBType;
import com.tailf.maapi.Maapi;
import com.tailf.maapi.MaapiUserSessionFlag;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.navu.NavuNode;
import com.tailf.navu.KeyPath2NavuNode;
import com.tailf.ncs.NcsMain;
import com.tailf.ncs.ns.Ncs;

public class ActionCommands {
  private static Logger LOGGER = Logger.getLogger(ActionCommands.class);

  private Maapi maapi;

  private void init() throws DpCallbackException {
    try {
      Socket socket = new Socket(NcsMain.getInstance().getNcsHost(),
                                 NcsMain.getInstance().getNcsPort());
      maapi = new Maapi(socket);
      maapi.startUserSession("", maapi.getSocket().getInetAddress(), "system",
          new String[] { "" }, MaapiUserSessionFlag.PROTO_TCP);
    } catch (Exception e) {
      throw new DpCallbackException(e);
    }
  }

  @ActionCallback(callPoint = "status-action", callType = ActionCBType.INIT)
  public void initStatusComplete(DpActionTrans trans)
      throws DpCallbackException {
    init();
  }

  @ActionCallback(callPoint = "status-action", callType = ActionCBType.ACTION)
  public ConfXMLParam[] statusComplete(DpActionTrans trans, ConfTag name,
      ConfObject[] kp, ConfXMLParam[] params) 
            throws DpCallbackException {
    try {

      // Refer to the service yang model prefix
      String nsPrefix = "nfv";

      int tid = maapi.startTrans(Conf.DB_RUNNING, Conf.MODE_READ);
      NavuContext context = new NavuContext(maapi,tid);
      NavuNode service = KeyPath2NavuNode.getNode(kp, context);

      String message = service.leaf(nfv._deploy_status).valueAsString();

      maapi.finishTrans(tid);

      return new ConfXMLParam[] {
          new ConfXMLParamValue(nsPrefix, "deployment-status", new ConfBuf(message))
      };

    } catch (Exception e) {
      throw new DpCallbackException(e);
    }
  }


  @ActionCallback(callPoint = "recovery-action", callType = ActionCBType.INIT)
  public void initRecoveryComplete(DpActionTrans trans)
      throws DpCallbackException {
    init();
  }

  
//  private Utility getParams(ConfKey key) throws IOException,
//      ConfException {
//    int tid = maapi.startTrans(Conf.DB_RUNNING, Conf.MODE_READ);
//    NavuContainer ncsRoot = new NavuContainer(maapi, tid, Ncs.hash);
//    NavuContainer megapopRoot = ncsRoot.getParent().container(nfv.uri);
//
//    Utility escParam = nfvRFS.getEscVM(megapopRoot.container(nfv._megapop)
//        .list(nfv._l2nid_mpls_csr).elem(key.elementAt(0).toString()));
//
//    maapi.finishTrans(tid);
//
//    return escParam;
//  }

  private void createFile(String filename, String tenant, Utility params) throws IOException {
    String line = String.format("%s %s %s %s", params.vm_group,
        params.service_name, "L2NID-MPLS-CSR", tenant);
    String path = "/tmp/" + filename;
    LOGGER.info(String.format("Writing '%s' to %s", line, path));
    BufferedWriter out = new BufferedWriter(new FileWriter(path));
    try {
      out.write(line);
      out.write("\n");
    } finally {
      out.close();
    }
  }
}
