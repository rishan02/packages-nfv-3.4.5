package com.singtel.dashboard;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.EnumSet;

import org.apache.log4j.Logger;

import com.singtel.nfv.namespaces.nfv;
import com.tailf.cdb.Cdb;
import com.tailf.cdb.CdbDiffIterate;
import com.tailf.cdb.CdbSubscription;
import com.tailf.cdb.CdbSubscriptionSyncType;
import com.tailf.cdb.CdbSubscriptionType;
import com.tailf.conf.ConfException;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfPath;
import com.tailf.conf.DiffIterateFlags;
import com.tailf.conf.DiffIterateOperFlag;
import com.tailf.conf.DiffIterateResultFlag;
import com.tailf.maapi.Maapi;
import com.tailf.maapi.MaapiUserSessionFlag;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.navu.NavuList;
import com.tailf.ncs.ApplicationComponent;
import com.tailf.ncs.annotations.Resource;
import com.tailf.ncs.annotations.ResourceType;
import com.tailf.ncs.annotations.Scope;
import com.tailf.ncs.ns.Ncs;

public class CsrDashboardSubscriber implements ApplicationComponent {

	private static Logger LOGGER = Logger
			.getLogger(CsrDashboardSubscriber.class);

	private CdbSubscription sub = null;
	// private CdbSession wsess;

	@Resource(type = ResourceType.CDB, scope = Scope.CONTEXT, qualifier = "reactive-fm-loop-subscriber")
	private Cdb cdb;

	@Resource(type = ResourceType.MAAPI, scope = Scope.INSTANCE, qualifier = "reactive-fm-m")
	private Maapi maapi;

	public void init() {

		LOGGER.info("Starting the CDB Connection...");

		// INSERT #### CDB-SUBSCRIBE
		try {
			// Start CDB session
			maapi.startUserSession("admin", InetAddress.getLocalHost(),
					"system", new String[] { "admin" },
					MaapiUserSessionFlag.PROTO_TCP);

			sub = cdb.newSubscription();
			// Subscribing to /megapop for any change in Csr Service
			sub.subscribe(1, new nfv(), "/nfv:megapop");
			sub.subscribe(CdbSubscriptionType.SUB_OPERATIONAL, 2, new nfv(),
					"/nfv:megapop/nfv:l2nid-mpls-csr/nfv:operational-status");

			// Tell CDB we are ready for notifications
			sub.subscribeDone();
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public void run() {
		LOGGER.info("Running the CDB subscriber...");

		try {
			while (true) {
				// Read the subscription socket for new events
				int[] points = null;
				try {
					// Blocking call, will throw an exception on package
					// reload/redeploy
					points = sub.read();
				} catch (ConfException e) {
					LOGGER.debug("Possible redeploy/reload of package, exiting");
					return;
				}
				// DiffIterateFlags tell our DiffIterator implementation what
				// values we want
				EnumSet<DiffIterateFlags> enumSet = EnumSet
						.<DiffIterateFlags> of(DiffIterateFlags.ITER_WANT_PREV,
								DiffIterateFlags.ITER_WANT_ANCESTOR_DELETE,
								DiffIterateFlags.ITER_WANT_SCHEMA_ORDER);
				ArrayList<Request> reqs = new ArrayList<Request>();
				try {
					// Iterate through the diff tree using the Iter class
					// reqs ArrayList is filled with requests for operations
					// (create, delete)
					sub.diffIterate(points[0], new Iter(), enumSet, reqs);
				} catch (Exception e) {
					LOGGER.error("", e);
					reqs = null;
				}

				@SuppressWarnings("deprecation")
				NavuContainer superRoot = new NavuContainer(
						new NavuContext(cdb));
				NavuContainer ncsRoot = superRoot.container(new Ncs().hash());
				// Loop through requests
				for (Request req : reqs) {

					if (req.path.toString().contains("megapop")) {
						DashboardUpdater csrdbUpdater = new DashboardUpdater();
						if (((req.op == Operation.CREATE) || (req.op == Operation.MODIFIED))
								&& isDashboardElement(req)) {
							LOGGER.debug("Operation :" + req.op
									+ " on service :" + req.key + "on leaf "
									+ req.leafToBeUpdated);
							// Add the values to the dashboard model
							csrdbUpdater.updateCsrDashboard(ncsRoot, req.key,
									req.csrDbOrDb, req.leafToBeUpdated);
						}

						if (req.op == Operation.DELETE) {
							NavuList serviceList = ncsRoot.getParent()
									.container(nfv.uri)
									.container(nfv._megapop_)
									.list(nfv._l2nid_mpls_csr_);
							if (!serviceList.containsNode(req.key)) {
								LOGGER.debug("Operation :" + req.op
										+ " on service :" + req.key);
								csrdbUpdater.deleteDashboardEntry(ncsRoot,
										req.key);
							} else if (isDashboardElement(req)) {

								LOGGER.debug("Operation :" + req.op
										+ " on service :" + req.key
										+ "on leaf " + req.leafToBeUpdated);
								csrdbUpdater.deleteDashboardLeaf(ncsRoot,
										req.key, req.csrDbOrDb,
										req.leafToBeUpdated);
							} else {
								LOGGER.debug("Operation :" + req.op
										+ " on service :" + req.key
										+ " not to be perfomerd");
							}
						}

					}
				}

				// Tell the subscription we are done
				sub.sync(CdbSubscriptionSyncType.DONE_PRIORITY);

			}

		} catch (SocketException e) {
			// silence here, normal close (redeploy/reload package)
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private boolean isDashboardElement(Request req) {
		String pathStr = req.path.toString();
		if (pathStr.contains("/brn")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._brn_;
			return true;
		} else if (pathStr.contains("/nfv-vas-reference")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._nfv_vas_reference_;
			return true;
		} else if (pathStr.contains("/ban")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._ban_;
			return true;
		} else if (pathStr.contains("/product-code")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._product_code_;
			return true;
		} else if (pathStr.contains("/service-type")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._service_type_;
			return true;
		} else if (pathStr.contains("/access-service-type")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._access_service_type_;
			return true;
		} else if (pathStr.contains("/site-code")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._site_code_;
			return true;
		} else if (pathStr.contains("/nfv-vas-scheme")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._nfv_vas_scheme_;
			return true;
		} else if (pathStr.contains("/brand")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._brand_;
			return true;
		} else if (pathStr.contains("/nfv-setup")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._nfv_setup_;
			return true;
		} else if (pathStr.contains("/nfv-access-setup")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._nfv_access_setup_;
			return true;
		} else if (pathStr.contains("/circuit-reference")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._circuit_reference_;
			return true;
		} else if (pathStr.contains("/deploy-status")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._deploy_status_;
			return true;
		} else if (pathStr.contains("/operational-status")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._operational_status_;
			return true;
		} else if (pathStr.contains("/speed")) {
			req.csrDbOrDb = "db";
			req.leafToBeUpdated = nfv._speed_;
			return true;
		} else if (pathStr.contains("/sid")) {
			req.csrDbOrDb = "csr";
			req.leafToBeUpdated = nfv._sid_;
			return true;
		} else if (pathStr.contains("/nickname")) {
			req.csrDbOrDb = "csr";
			req.leafToBeUpdated = nfv._nickname_;
			return true;
		} else if (pathStr.contains("/service-name")) {
			req.csrDbOrDb = "csr";
			req.leafToBeUpdated = nfv._service_name_;
			return true;
		} else if (pathStr.contains("/network-service")) {
			req.csrDbOrDb = "csr";
			req.leafToBeUpdated = nfv._network_service_;
			return true;
		} else
			return false;
	}

	public void finish() {
		try {
			cdb.close();
			maapi.getSocket().close();
		} catch (Exception e1) {

		}
		LOGGER.info("Finished.");

	}

	private enum Operation {
		CREATE, DELETE, MODIFIED
	}

	private class Request {
		ConfKey key;
		Operation op;
		ConfPath path;
		String leafToBeUpdated;
		String csrDbOrDb;
	}

	private class Iter implements CdbDiffIterate {

		public DiffIterateResultFlag iterate(ConfObject[] kp,
				DiffIterateOperFlag op, ConfObject oldValue,
				ConfObject newValue, Object initstate) {

			@SuppressWarnings("unchecked")
			ArrayList<Request> reqs = (ArrayList<Request>) initstate;

			try {
				ConfPath p = new ConfPath(kp);
				ConfKey key = (ConfKey) kp[kp.length - 3];
				Request r = new Request();
				r.path = p;
				r.key = key;
				if ((op == DiffIterateOperFlag.MOP_CREATED) && (kp.length == 3)) {
					r.op = Operation.CREATE;
					reqs.add(r);
				}

				// The service instance (l2vpn list child node) is deleted
				else if ((op == DiffIterateOperFlag.MOP_DELETED)) {
					r.op = Operation.DELETE;
					reqs.add(r);
				} else if ((op == DiffIterateOperFlag.MOP_MODIFIED)
						|| (op == DiffIterateOperFlag.MOP_VALUE_SET)) {
					r.op = Operation.MODIFIED;
					reqs.add(r);
				}

			} catch (Exception e) {
				LOGGER.error("", e);
			}
			return DiffIterateResultFlag.ITER_RECURSE;

		}

	}

}
