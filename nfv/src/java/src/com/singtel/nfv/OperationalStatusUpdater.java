package com.singtel.nfv;

import com.singtel.nfv.notifCdbSubscriber.Request;
import com.singtel.nfv.namespaces.nfv;
import com.tailf.conf.Conf;
import com.tailf.maapi.Maapi;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.navu.NavuList;

public class OperationalStatusUpdater {

	void serviceOperationalStatusUpdate(Request req, Maapi maapi) {

		try {
			NavuContext operContext = new NavuContext(maapi);
			int th = operContext.startOperationalTrans(Conf.MODE_READ_WRITE);
			NavuContainer mroot = new NavuContainer(operContext);
			NavuList serviceList = mroot.container(nfv.uri)
					.container(nfv._megapop_).list(nfv._l2nid_mpls_csr_);
			if (serviceList.containsNode(req.serviceName)) {
				NavuContainer serviceInstance = serviceList
						.elem(req.serviceName);
				serviceInstance.leaf("operational-status").set(req.event);
			}
			maapi.applyTrans(th, false);
			maapi.finishTrans(th);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
