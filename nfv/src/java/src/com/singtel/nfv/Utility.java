package com.singtel.nfv;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import com.tailf.conf.ConfException;
import com.tailf.dp.DpCallbackException;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuException;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;
import com.singtel.nfv.namespaces.nfv;
import com.tailf.conf.Conf;
import com.tailf.conf.ConfPath;
import com.tailf.maapi.Maapi;
import com.tailf.maapi.MaapiUserSessionFlag;
import com.tailf.navu.NavuContext;
import com.tailf.ncs.NcsMain;
import com.tailf.ncs.ns.Ncs;
import java.net.Socket;

public class Utility {
	private static Logger LOGGER = Logger.getLogger(Utility.class);
	public String service_name;
	public String service_version;
	public String vm_group;

	public Utility(String service_name, String service_version, String vm_group) {
		this.service_name = service_name;
		this.service_version = service_version;
		this.vm_group = vm_group;
	}

	public Utility() {
		service_name = Commons.defaultEscServiceName;
		service_version = Commons.defaultEscServiceVersion;
		vm_group = Commons.defaultEscVMGroup;
	}

	public String toString() {
		return String.format("%s(%s) - %s", service_name, service_version, vm_group);
	}

	public static String getIPAddress(String prefix) {
		String[] parts = prefix.split("/");
		return parts[0];
	}

	public static String getNetAddr(String addr) throws UnknownHostException {
		String[] parts = addr.split("/");
		int prefix;
		if (parts.length < 2) {
			prefix = 0;
		} else {
			prefix = Integer.parseInt(parts[1]);
		}
		int shift = 32 - prefix;
		String[] nums = parts[0].split("\\.");
		int i = (Integer.parseInt(nums[0]) << 24 | Integer.parseInt(nums[2]) << 8 | Integer.parseInt(nums[1]) << 16
				| Integer.parseInt(nums[3]));
		int net = (i >> shift) << shift;
		int value = net;
		byte[] bytes = new byte[] { (byte) (value >>> 24), (byte) (value >> 16 & 0xff), (byte) (value >> 8 & 0xff),
				(byte) (value & 0xff) };
		InetAddress netAddr = InetAddress.getByAddress(bytes);
		return netAddr.getHostAddress();
	}

	public static String getNetMask(String addr) throws UnknownHostException {
		String[] parts = addr.split("/");
		int prefix;
		if (parts.length < 2) {
			prefix = 0;
		} else {
			prefix = Integer.parseInt(parts[1]);
		}
		int mask;

		if (prefix == 0) {
			mask = 0x0 << (32 - prefix);
		} else
			mask = 0xffffffff << (32 - prefix);

		int value = mask;
		byte[] bytes = new byte[] { (byte) (value >>> 24), (byte) (value >> 16 & 0xff), (byte) (value >> 8 & 0xff),
				(byte) (value & 0xff) };
		InetAddress netAddr = InetAddress.getByAddress(bytes);
		return netAddr.getHostAddress();
	}

	public static String getWildCardMask(String mask) throws Exception {
		String[] parts = mask.split("\\.");
		int p1 = 255 - Integer.parseInt(parts[0]);
		int p2 = 255 - Integer.parseInt(parts[1]);
		int p3 = 255 - Integer.parseInt(parts[2]);
		int p4 = 255 - Integer.parseInt(parts[3]);
		return (String.valueOf(p1) + "." + String.valueOf(p2) + "." + String.valueOf(p3) + "." + String.valueOf(p4));
	}

	public static String getHostName(NavuNode ncsRoot, String sidInput, String csrName)
			throws ConfException, NavuException, IOException {

		String hostName = "";
		String sid = sidInput.replaceAll("[^a-zA-Z0-9-]+", "");
		LOGGER.info("sid with aplhanumerics and hyphen: " + sid);
		if (sid.substring(0, 1).matches("\\d")) {
			sid = "v" + sid;
		}
		hostName = sid.substring(0, Math.min(sid.length(), 16));
		int l = hostName.length();
		if (hostName.substring(l - 1, l).equalsIgnoreCase("-")) {
			if (sid.length() > 16) {
				sid = sid.substring(15, sid.length()).replaceAll("-", "");
				LOGGER.info("hyphen removed from sid after 16 characters: " + sid);
				hostName = hostName.substring(0, l - 1) + sid.substring(0, 1);

			} else {
				hostName = hostName.substring(0, l - 1);
			}
		}
		LOGGER.info("Host name : " + hostName);
		NavuList devices = ncsRoot.container("ncs", "devices").list("device");
		for (NavuContainer device : devices) {
			String existingHostName = device.container("config").leaf("ios", "hostname").valueAsString();
			try {
				if (!device.toString().contains(csrName)) {
					if (existingHostName != null) {
						if (existingHostName.equals(hostName)) {
							throw new DpCallbackException(String
									.format("Host name is not unique:  %1$s :" + "Please check the SID", hostName));
						}
					}
				} else {
					LOGGER.info("Skipped the current device: " + csrName + " host name: " + existingHostName);
				}
			} catch (Exception e) {
				LOGGER.info("Excpetion in host name check");
			}
		}
		return hostName;
	}

	/**
	 * checks if the service is up and running
	 */
	protected static String getDeploymentPlanStatus(NavuNode service, String csrName, String tenant, String esc) {
		Maapi m = null;
		NavuNode readyState = null;
		String serviceReadyState = Commons.SERVICE_CREATION_NOT_REACHED;
		try {
			Socket socket = new Socket(service.context().getMaapi().getSocket().getInetAddress(),
					service.context().getMaapi().getSocket().getPort());
			m = new Maapi(socket);
			m.startUserSession(Commons.admin, m.getSocket().getInetAddress(), Commons.system,
					new String[] { Commons.admin }, MaapiUserSessionFlag.PROTO_TCP);
			NavuContext ctx = new NavuContext(m);
			ctx.startOperationalTrans(Conf.MODE_READ);
			NavuNode opRoot = new NavuContainer(ctx);
			String readyPath = String.format(Commons.SERVICE_READY_STATE_PATH, tenant, csrName, esc);
			String serviceName = getValue(service, "service-name");

			try {
				readyState = opRoot.getNavuNode(new ConfPath(readyPath));
			} catch (NavuException e) {
				LOGGER.info("Service " + serviceName + " is not yet available.");
				return serviceReadyState;
			}
			serviceReadyState = getValue(readyState, Ncs._status_);
			LOGGER.info("Service ready state: " + serviceReadyState);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Helper.safeclose(m);
		}
		return serviceReadyState;
	}

	/**
	 * Fetches the value based on key
	 */
	private static String getValue(NavuNode node, String key) throws NavuException {
		return node.leaf(key).valueAsString();
	}

	protected static boolean isServiceExists(String serviceName) {
		return (getServiceStatus(serviceName) == null) ? false : true;
	}

	/**
	 * get Service deploy-status
	 */
	protected static String getServiceStatus(String serviceName) {
		String readyPath = String.format("/megapop/l2nid-mpls-csr{%s}", serviceName);
		Maapi maapi = null;
		String serviceStatus = null;
		try {
			Socket socket = new Socket(NcsMain.getInstance().getNcsHost(), NcsMain.getInstance().getNcsPort());
			maapi = new Maapi(socket);
			maapi.startUserSession(Commons.admin, maapi.getSocket().getInetAddress(), Commons.system,
					new String[] { Commons.admin }, MaapiUserSessionFlag.PROTO_TCP);
			NavuContext ctx = new NavuContext(maapi);
			ctx.startOperationalTrans(Conf.MODE_READ);
			NavuNode opRoot = new NavuContainer(ctx);
			// throws exceptions if readyPath does not exists
			NavuNode service = opRoot.getNavuNode(new ConfPath(readyPath)); 
			try {
				service = opRoot.getNavuNode(new ConfPath(readyPath));
				serviceStatus = service.leaf(nfv._deploy_status_).valueAsString();
			} catch (Exception e) {
				LOGGER.info("Service " + serviceName + " is not available for deletion");
			}
			LOGGER.info("Service status:" + serviceStatus);
		} catch (Exception e) {
			return serviceStatus;
		}
		return serviceStatus;
	}
}