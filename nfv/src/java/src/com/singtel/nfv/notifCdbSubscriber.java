package com.singtel.nfv;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.EnumSet;

import org.apache.log4j.Logger;

import com.tailf.cdb.Cdb;
import com.tailf.cdb.CdbDiffIterate;
import com.tailf.cdb.CdbSubscription;
import com.tailf.cdb.CdbSubscriptionSyncType;
import com.tailf.cdb.CdbSubscriptionType;
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

public class notifCdbSubscriber implements ApplicationComponent {
	private static Logger LOGGER = Logger.getLogger(notifCdbSubscriber.class);

	private CdbSubscription sub = null;

	@Resource(type = ResourceType.CDB, scope = Scope.INSTANCE, qualifier = "esc-notif-fm-loop-subscriber")
	private Cdb cdb;

	@Resource(type = ResourceType.MAAPI, scope = Scope.INSTANCE, qualifier = "reactive-fm-m")
	private Maapi maapi;

	public notifCdbSubscriber() {
	}

	public void init() {
		try {
			LOGGER.info("starting notif subscriber");

			sub = cdb.newSubscription();
			String s = "/ncs:devices/device/netconf-notifications/"
					+ "received-notifications";

			// Start CDB session
			maapi.startUserSession("admin", InetAddress.getLocalHost(), "esc",
					new String[] { "admin" }, MaapiUserSessionFlag.PROTO_TCP);
			sub.subscribe(CdbSubscriptionType.SUB_OPERATIONAL, 1, Ncs.hash, s);

			sub.subscribeDone();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	public void run() {
		try {

			while (true) {
				int[] points = null;

				try {
					// Blocking call, will throw an exception on package
					// reload/redeploy
					points = sub.read();
				} catch (Exception e) {
					LOGGER.debug("Possible redeploy/reload of package, exiting");
					return;
				}

				EnumSet<DiffIterateFlags> enumSet = EnumSet.of(
						DiffIterateFlags.ITER_WANT_PREV,
						DiffIterateFlags.ITER_WANT_ANCESTOR_DELETE,
						DiffIterateFlags.ITER_WANT_SCHEMA_ORDER);

				try {

					sub.diffIterate(points[0], new Iter(sub), enumSet, null);

				} finally {
					sub.sync(CdbSubscriptionSyncType.DONE_OPERATIONAL);
				}
			}
		} catch (SocketException e) {
			// silence here, normal close (redeploy/reload package)
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	public void finish() {
		try {
			cdb.close();
			maapi.getSocket().close();
		} catch (Exception e1) {

			e1.printStackTrace();
		}
		LOGGER.info("Finished.");
	}

	/*
	 * private void safeclose(Cdb s) { try { s.close(); } catch (Exception
	 * ignore) { } }
	 */

	public class Request {
		String serviceName;
		String esc;
		ConfPath path;
		String tenant;
		String event;
		String depName;
	}

	private class Iter implements CdbDiffIterate {

		Iter(CdbSubscription sub) {
		}

		public DiffIterateResultFlag iterate(ConfObject[] kp,
				DiffIterateOperFlag op, ConfObject oldValue,
				ConfObject newValue, Object initstate) {

			try {
				// Declaring the objects for deleting tenant and to update
				// operational status
				OperationalStatusUpdater osUpdater = new OperationalStatusUpdater();

				// Request object to store details about the current
				// notification
				Request r = new Request();
				r.esc = kp[kp.length - 3].toString();

				// Getting the ncsRoot from cdb
				@SuppressWarnings("deprecation")
				NavuContainer superRoot = new NavuContainer(
						new NavuContext(cdb));
				NavuContainer ncsRoot = superRoot.container(new Ncs().hash());

				// Getting the notifList from nutofNumber in kp
				NavuContainer notif;
				ConfKey notifNumber = (ConfKey) kp[kp.length - 7];
				NavuList notifList = ncsRoot.container("devices")
						.list("device").elem(r.esc)
						.container("netconf-notifications")
						.container("received-notifications")
						.list("notification");

				// Nofication number doesn't exist, do not process the
				// notification and return.
				if (notifList.containsNode(notifNumber)) {
					notif = notifList.elem(notifNumber).container("data")
							.container("esc", "escEvent");
				} else {
					return DiffIterateResultFlag.ITER_CONTINUE;
				}

				// Update all values in Request object
				NavuContainer e = notif.container("event");
				r.serviceName = null;
				r.event = e.leaf("type").valueAsString();
				r.tenant = notif.leaf("tenant").valueAsString();
				r.depName = notif.leaf("depname").valueAsString();

				if (op == DiffIterateOperFlag.MOP_CREATED) {

					// If depName is null, we do not have ServiceName , ignore
					// the notification
					if (r.depName == null) {
						LOGGER.info("Dropping notif because no depname is set: "
								+ e.leaf("type").valueAsString());
						return DiffIterateResultFlag.ITER_CONTINUE;
					} else {
						LOGGER.info("Depname is set , so setting it to Operational status: "
								+ e.leaf("type").valueAsString());

						LOGGER.info("Depname is set , so setting it to Operational status: "
								+ e.leaf("type").valueAsString());

						String searchInDepName = "_"
								+ Commons.deploymentName + "_"
								+ Commons.csr;
						r.serviceName = r.depName.substring(0,
								r.depName.indexOf(searchInDepName));
						osUpdater.serviceOperationalStatusUpdate(r, maapi);
					

					}
					LOGGER.debug("Notif Received");
					return DiffIterateResultFlag.ITER_CONTINUE;
				}
			} catch (Exception e) {
				LOGGER.error("notif ITER:", e);
			}

			return DiffIterateResultFlag.ITER_RECURSE;

		}
	}
}