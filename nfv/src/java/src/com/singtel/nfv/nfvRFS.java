package com.singtel.nfv;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cisco.singtel.resourcemanager.ResourceAllocator;
import com.singtel.nfv.namespaces.nfv;
import com.tailf.cdb.Cdb;
import com.tailf.conf.Conf;
import com.tailf.conf.ConfBuf;
import com.tailf.conf.ConfException;
import com.tailf.conf.ConfList;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfUInt32;
import com.tailf.conf.ConfXMLParam;
import com.tailf.conf.ConfXMLParamValue;
import com.tailf.dp.DpCallbackException;
import com.tailf.dp.annotations.ServiceCallback;
import com.tailf.dp.proto.ServiceCBType;
import com.tailf.dp.services.ServiceContext;
import com.tailf.dp.services.ServiceOperationType;
import com.tailf.maapi.Maapi;
import com.tailf.maapi.MaapiCrypto;
import com.tailf.maapi.MaapiUserSessionFlag;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuContext;
import com.tailf.navu.NavuException;
import com.tailf.navu.NavuLeaf;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;
import com.tailf.ncs.NcsMain;
import com.tailf.ncs.PlanComponent;
import com.tailf.ncs.annotations.Resource;
import com.tailf.ncs.annotations.ResourceType;
import com.tailf.ncs.annotations.Scope;
import com.tailf.ncs.ns.Ncs;
import com.tailf.ncs.template.Template;
import com.tailf.ncs.template.TemplateVariables;

public class nfvRFS {
	private static Logger LOGGER = Logger.getLogger(nfvRFS.class);
	@Resource(type = ResourceType.CDB, scope = Scope.INSTANCE, qualifier = "nfv-cdb")
	private Cdb cdb;

	// Call back method which runs before performing any modification to the
	// service
	@ServiceCallback(servicePoint = Commons.servicePoint, callType = ServiceCBType.PRE_MODIFICATION)
	public Properties preModification(ServiceContext context, ServiceOperationType operation, ConfPath path,
			Properties opaque) throws UnknownHostException, IOException, ConfException {
		try {
			if (operation == ServiceOperationType.DELETE) {
				LOGGER.info("*** Undeployment of service starts *** ");
				checkDeviceSync(context, opaque.getProperty("esc"));
				checkDeviceSync(context, opaque.getProperty("dci"));
				String csr = opaque.getProperty("csr");
				String serviceName = opaque.getProperty("service-name");
				String serviceStatus = Utility.getServiceStatus(serviceName);
				if (serviceStatus == null) {
					LOGGER.info("Service " + serviceName + " is not available for deletion");
				} else if (serviceStatus.equals(Commons.inProgress)) {
					throw new DpCallbackException(
							String.format("Deletion is not allowed as VNF creation is in progress"));
				}

				if (csr != null)
					deregisterSmartLicense(csr, context);
				LOGGER.info("*** Undeployment of service end *** ");
			}
		} catch (Exception e) {
			throw new DpCallbackException(Commons.cannotDeleteService + e.getMessage());
		}
		return opaque;
	}

	/**
	 * Create callback method. This method is called when a service instance
	 * committed due to a create or update event.
	 *
	 * This method returns a opaque as a Properties object that can be null. If
	 * not null it is stored persistently by Ncs. This object is then delivered
	 * as argument to new calls of the create method for this service (fastmap
	 * algorithm). This way the user can store and later modify persistent data
	 * outside the service model that might be needed.
	 *
	 * @param context
	 *            - The current ServiceContext object
	 * @param service
	 *            - The NavuNode references the service node.
	 * @param ncsRoot
	 *            - This NavuNode references the ncs root.
	 * @param opaque
	 *            - Parameter contains a Properties object. This object may be
	 *            used to transfer additional information between consecutive
	 *            calls to the create callback. It is always null in the first
	 *            call. I.e. when the service is first created.
	 * @return Properties the returning opaque instance
	 * @throws ConfException
	 */

	@ServiceCallback(servicePoint = Commons.servicePoint, callType = ServiceCBType.CREATE)
	public Properties create(ServiceContext context, NavuNode service, NavuNode ncsRoot, Properties opaque)
			throws ConfException {
		LOGGER.info("***** Create/Reactive-re-deploy ************");
		LOGGER.info("Service name : " + getValue(service, nfv._service_name_));
		PlanComponent selfPlan = new PlanComponent(service, "self", "ncs:self");
		selfPlan.appendState("ncs:init");
		selfPlan.appendState("ncs:ready");
		selfPlan.setReached("ncs:init");
		changeDeployStatus(service, Commons.inProgress);
		String sitecode = getAndLogValue(service, nfv._site_code_);
		String podName = getAndLogValue(service, nfv._pod_);
		String dciName = getAndLogValue(service, nfv._dci_);
		String dciLinkEndPoint = getAndLogValue(service, nfv._dci_link_end_point_);
		String networkName = getAndLogValue(service, nfv._access_service_type_);
		String escName = null;
		NavuList links = null;
		String vnfPhysicalNetwork = null;
		String mgmtNetworkName = null;
		String authGroup = null;
		String sharedTenant = null;
		String alarmDelay = null;
		String ifNameIn = null;
		String ifNameOut = null;
		String ifTypeOut = null;

		NavuContainer sites = ncsRoot.getParent().container(Commons.sitesUri).container("sites");
		NavuList siteList = sites.list("site");
		String serviceName = getValue(service, nfv._service_name_);
		for (NavuContainer site : siteList.elements()) {
			if (sitecode != null && sitecode.equals(site.leaf("site-code").valueAsString())) {
				if (podName == null) {
					podName = getAndLogValue(site, "preferred-pod");
				}
				NavuList podList = site.container("pods").list("pod");
				for (NavuContainer pod : podList.elements()) {
					if (podName != null && podName.equals(pod.leaf("pod-name").valueAsString())) {
						escName = getAndLogValue(pod, "esc");
						vnfPhysicalNetwork = getAndLogValue(pod, "vnf-physical-network");
						mgmtNetworkName = getAndLogValue(pod, "mgmt-network-name");
						if (dciName == null)
							dciName = getAndLogValue(pod, "preferred-dci");
						for (NavuContainer dci : pod.list("dci")) {
							if (dciName != null && dciName.equals(dci.leaf("dci-name").valueAsString())) {
								links = dci.list("dci-link");
								if (dciLinkEndPoint == null) {
									if (networkName.equals("megapop")) {
										dciLinkEndPoint = getAndLogValue(dci, "preferred-megapop-link");
									} else if (networkName.equals("singnet")) {
										dciLinkEndPoint = getAndLogValue(dci, "preferred-singnet-link");
									} else if (networkName.equals("cplus")) {
										dciLinkEndPoint = getAndLogValue(dci, "preferred-cplus-link");
									}
								}
								for (NavuContainer dciLink : links.elements()) {
									if (dciLinkEndPoint != null && dciLinkEndPoint
											.equals(dciLink.leaf("link-end-point-id").valueAsString())) {
										ifNameIn = getAndLogValue(dciLink, "if-name-in");
										ifNameOut = getAndLogValue(dciLink, "if-name-out");
										ifTypeOut = getAndLogValue(dciLink, "if-type-out");
									}
								}
							}
						}
					}
				}
			}
		}

		NavuList configParamsList = sites.list("sys-config-params");
		for (NavuContainer configParam : configParamsList.elements()) {
			if (configParam.leaf("param-name").valueAsString().equals("auth-group")) {
				authGroup = getAndLogValue(configParam, "param-value");
			}
			if (configParam.leaf("param-name").valueAsString().equals("shared-tenant")) {
				sharedTenant = getAndLogValue(configParam, "param-value");
			}
			if (configParam.leaf("param-name").valueAsString().equals("alarm-delay")) {
				alarmDelay = getAndLogValue(configParam, "param-value");
			}
		}

		if (podName == null || dciName == null || dciLinkEndPoint == null) {
			throw new DpCallbackException(
					"Please configure either pod/preferred dci/preferred-dci-link-end-point configured in site topology or provide pod/dci/dci-link-end-point values with service creation ");
		}
		if (authGroup == null || sharedTenant == null) {
			throw new DpCallbackException(
					"Please check whether auth group/shared tenant configured in site topology or not");
		}
		if (alarmDelay == null) {
			throw new DpCallbackException("Please configure alarm Delay in minutes");
		}

		try {
			checkDeviceSync(context, escName);
			checkDeviceSync(context, dciName);
		} catch (DpCallbackException e) {
			throw (DpCallbackException) e;
		} catch (Exception e) {
			throw new DpCallbackException(Commons.notAbleToCheckDevices, e);
		}

		if (opaque == null)
			opaque = new Properties();

		opaque.setProperty("esc", escName);
		opaque.setProperty("dci", dciName);
		opaque.setProperty("service-name", serviceName);
		try {
			Maapi maapi;
			Template serviceRedeployKicker = new Template(context, Commons.serviceRedeployKicker);
			TemplateVariables vars = new TemplateVariables();

			String hostName = "";
			ConfUInt32 ingressVlanID;
			ConfUInt32 egressVlanID;
			String brn = getAndLogValue(service, nfv._brn_);
			String ban = getAndLogValue(service, nfv._ban_);
			String sid = getAndLogValue(service, nfv._sid_);
			String lanIp = getAndLogValue(service, nfv._lan_ip_);
			String wanIp = getAndLogValue(service, nfv._wan_ip_);
			String deploymentName = Commons.deploymentName;
			String tenant = "";

			// Updating the topology instance for the service
			NavuContainer topology = service.container(nfv._topology);
			NavuContainer lanCon = topology.container(nfv._lan_connection);
			NavuContainer wanCon = topology.container(nfv._wan_connection);

			// Create CSR VM name
			String csrName = Helper.makeDevName(serviceName, Commons.deploymentName, Commons.csr, escName);
			String ingressNet = csrName + Commons.ingress;
			String egressNet = csrName + Commons.egress;
			LOGGER.info("Deployment name : " + csrName);
			LOGGER.info("Csr name : " + csrName);
			LOGGER.info("Ingress network : " + ingressNet);
			LOGGER.info("Egress network  : " + egressNet);

			if (sid != null)
				hostName = Utility.getHostName(ncsRoot, sid, csrName);
			// check to know static or dynamic tenant
			boolean dynamicTenant = checkTenant(ncsRoot, csrName, escName, sharedTenant);

			if (dynamicTenant) {
				tenant = ban;
			} else {
				tenant = sharedTenant;
			}

			opaque.setProperty("tenant", tenant);
			LOGGER.info("Tenant name : " + tenant);
			LOGGER.info("vnfd name : " + Commons.VNFD_NAME);
			LOGGER.info("vdu name : " + Commons.VDU_NAME);
			NavuLeaf currentName = service.leaf("device-name");
			currentName.sharedSet(tenant + "-" + serviceName + "_L2M_CSR_" + escName + "-" + Commons.VNFD_NAME + "-"
					+ Commons.VDU_NAME + "-" + escName + "-1");

			// kicker for service re-deploy
			vars.putQuoted("TENANT", tenant);
			vars.putQuoted("SERVICE_NAME", serviceName);
			vars.putQuoted("DEPLY_NAME", csrName);
			vars.putQuoted("ESC", escName);
			serviceRedeployKicker.apply(service, vars);

			maapi = context.getRootNode().context().getMaapi();
			int th = context.getRootNode().context().getMaapiHandle();
			String mgmtIP = ResourceAllocator.getInstance().getAllocatedManagementIp(maapi, th, sitecode, podName,
					serviceName);
			ingressVlanID = new ConfUInt32(ResourceAllocator.getInstance().getAllocatedVlanId(maapi, th, sitecode,
					podName, dciName, dciLinkEndPoint, serviceName, true));
			egressVlanID = new ConfUInt32(ResourceAllocator.getInstance().getAllocatedVlanId(maapi, th, sitecode,
					podName, dciName, dciLinkEndPoint, serviceName, false));

			LOGGER.info("Allocated ingress vlan id :" + ingressVlanID.longValue());
			LOGGER.info("Allocated egress vlan id :" + egressVlanID.longValue());
			// If VLAN ID allocation is successful then set VLAN network
			// parameters to topology in service instance
			if (ingressVlanID != null && egressVlanID != null) {
				lanCon.leaf(Commons.vlan).sharedSet(ingressVlanID);
				lanCon.leaf(Commons.osNet).sharedSet(ingressNet);
				wanCon.leaf(Commons.vlan).sharedSet(egressVlanID);
				wanCon.leaf(Commons.osNet).sharedSet(egressNet);

				// Create OpenStack networks for service instance
				// If all parameters for service instance is submitted
				// set other topology pramaters in service instance
				if (brn != null && ban != null && sid != null && lanIp != null && wanIp != null) {
					LOGGER.info("*** Links size ****" + links.size());
					for (NavuContainer link : links) {
						if (link.leaf("link-end-point-id") != null
								&& link.leaf("link-end-point-id").valueAsString().equals(dciLinkEndPoint)) {
							addDciToTopology(lanCon, link, ingressVlanID.longValue(), dciName);
							addDciToTopology(wanCon, link, egressVlanID.longValue(), dciName);
							break;
						}
					}

					String serviceReadyState = Utility.getDeploymentPlanStatus(service, csrName, tenant, escName);
					String deviceFullName = tenant + "-" + csrName + "-" + Commons.VNFD_NAME + "-" + Commons.VDU_NAME
							+ "-" + escName + "-1";

					if (opaque.getProperty("serviceMonitor") == null) {
						new ServiceMonitor().checkServiceState(alarmDelay, service, csrName, deviceFullName, tenant,
								escName);
						opaque.setProperty("serviceMonitor", "enable");
					}

					boolean serviceCreationFailed = Commons.SERVICE_CREATION_FAILED.equals(serviceReadyState);
					boolean serviceCreateNotReachded = Commons.SERVICE_CREATION_NOT_REACHED.equals(serviceReadyState);
					boolean serviceCreateReached = Commons.SERVICE_CREATION_SUCCESS.equals(serviceReadyState);

					// this if condition helps in rollback incase of incomlete
					// service creation
					if (!serviceCreationFailed && (serviceCreateNotReachded || serviceCreateReached)) {
						createCsrVMmano(context, service, ncsRoot, csrName, tenant, deploymentName, escName, mgmtIP,
								lanIp, wanIp, ingressVlanID, egressVlanID, ingressNet, egressNet, authGroup,
								mgmtNetworkName, vnfPhysicalNetwork);
					}

					if (serviceCreationFailed) {
						selfPlan.setFailed("ncs:ready");
						changeDeployStatus(service, Commons.nonOperational);
						String alarmText = "Service creation failed for the service: " + serviceName + ", device:"
								+ deviceFullName;
						AlarmClient.raiseAlarm(deviceFullName, alarmText);
					}

					if (serviceCreateReached) {
						// we are framing csrName to match with nfvo bundle
						// create csr name
						csrName = tenant + "-" + csrName + "-" + Commons.VNFD_NAME + "-" + Commons.VDU_NAME + "-"
								+ escName + "-1";
						opaque.setProperty("csr", csrName);
						LOGGER.info("Service is alive now, apply Day-N configuration on : " + csrName);

						// Set CSR parameters in service topology instance
						addCsrToTopology(lanCon, csrName, Commons.gigabitEthernet, Commons.two, lanIp);
						addCsrToTopology(wanCon, csrName, Commons.gigabitEthernet, Commons.three, wanIp);

						DayOneConf dayOne = new DayOneConf(context, service, csrName, hostName);
						dayOne.applyConfiguration();

						// Create DCI sub-interfaces and l2vpn x-connect setting
						applyDciTemplate(service, context, dciName, ifNameIn, ifNameOut, ifTypeOut,
								egressVlanID.toString(), ingressVlanID.toString());

						changeDeployStatus(service, Commons.operational);
						selfPlan.setReached("ncs:ready");

						DayTwoConf dayTwo = new DayTwoConf(context, service, csrName);
						dayTwo.applyConfiguration();

						DayThreeConf dayThree = new DayThreeConf(context, service, csrName);
						dayThree.applyConfiguration();
						LOGGER.info("service deployment finished: " + csrName);
					}
				} else {
					LOGGER.info("Service paramters for " + serviceName + " are not ready, wait for re-deploy");
					changeDeployStatus(service, Commons.nonOperational);
				}
			} else
				changeDeployStatus(service, Commons.nonOperational);
		} catch (Exception e) {
			throw new DpCallbackException(e.getMessage(), e);
		}
		return opaque;
	}

	@ServiceCallback(servicePoint = Commons.servicePoint, callType = ServiceCBType.POST_MODIFICATION)
	public Properties postModification(ServiceContext context, ServiceOperationType operation, ConfPath path,
			Properties opaque) throws UnknownHostException, IOException, ConfException {
		if (operation != ServiceOperationType.DELETE) {
			try {
				LOGGER.info("In post modification call");
				NavuNode service = context.getServiceNode();

				service.list(nfv._interface).safeCreate("3");
				service.list(nfv._interface).safeCreate("2");
				for (NavuNode cVlan : service.list("lan-cvlans")) {
					String lancvanvalId = cVlan.leaf("lan-cvlan-id").valueAsString();
					service.list(nfv._interface).safeCreate("3." + lancvanvalId);
					LOGGER.info("setting lan subinterface id :" + "3." + lancvanvalId);
				}
				for (NavuNode cVlan : service.list("wan-cvlans")) {
					String wancvanvalId = cVlan.leaf("wan-cvlan-id").valueAsString();
					service.list(nfv._interface).safeCreate("2." + wancvanvalId);
					LOGGER.info("setting wan subinterface id :" + "2." + wancvanvalId);
				}

				String routingProtocol = service.leaf(nfv._routing_protocol).valueAsString();
				if (routingProtocol != null) {
					if (routingProtocol.equals("bgp")) {
						if (opaque.getProperty("bgpProcess") == null) {
							String asNumber = service.leaf(nfv._as_number).valueAsString();
							String remoteAsNumber = service.leaf(nfv._remote_as_number).valueAsString();
							String neighborPasswd = service.leaf(nfv._neighbor_passwd).valueAsString();
							String gateway = service.leaf(nfv._gateway).valueAsString();
							LOGGER.info("bgp process is null");
							NavuContainer bgpProcess = service.list(nfv._bgp).safeCreate(asNumber);
							NavuContainer bgpNb = bgpProcess.list("neighbor").safeCreate(gateway);
							bgpNb.leaf("remote-as-number").set(remoteAsNumber);
							bgpNb.leaf("password").set(neighborPasswd);
							NavuContainer bgpAf = bgpProcess.list("address-family").safeCreate("ipv4");
							NavuContainer afNb = bgpAf.list("neighbor").safeCreate(gateway);
							afNb.leaf("activate").sharedSet("enable");
							opaque.setProperty("bgpProcess", "true");
							LOGGER.info("bgp process initialized");
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("exception while writing creating or updating service or bgp process");
			}
		}
		return opaque;
	}

    private void createCsrVMmano(ServiceContext context, NavuNode service, NavuNode ncsRoot, String csrName,
            String tenant, String deploymentName, String esc, String mgmtIP, String lanIp, String wanIp,
            ConfUInt32 ingressVlanId, ConfUInt32 egressVlanId, String ingressNet, String egressNet, String authGroup,
            String mgmtNetworkName, String vnfPhysicalNetwork) throws UnknownHostException, IOException, ConfException {
        Maapi m = null;
        Template createCsrVmTemplate = new Template(context, Commons.createCsrVmTemplate);
        TemplateVariables vars = new TemplateVariables();
        try {
            LOGGER.info(csrName + " is being created.");
            String lanValue = lanIp.substring(0, lanIp.indexOf('/'));
            String wanValue = wanIp.substring(0, wanIp.indexOf('/'));
            String lanMask = lanIp.substring(lanIp.indexOf('/'));
            String wanMask = wanIp.substring(wanIp.indexOf('/'));
            String ingressMacAddress = getAndLogValue(service, nfv._ingress_mac_address_);
            String egressMacAddress = getAndLogValue(service, nfv._egress_mac_address_);

            if (ingressMacAddress == null || egressMacAddress == null) {
                LOGGER.error("Note: ingress_mac_address: "+ ingressMacAddress +" or egress_mac_address: "+ egressMacAddress +" not provided");
            }

            ingressMacAddress = (ingressMacAddress == null) ? "" : ingressMacAddress;
            egressMacAddress = (egressMacAddress == null) ? "" : egressMacAddress;

            Socket socket = new Socket(service.context().getMaapi().getSocket().getInetAddress(),
                    service.context().getMaapi().getSocket().getPort());
            m = new Maapi(socket);
            m.startUserSession(Commons.admin, m.getSocket().getInetAddress(), Commons.system,
                    new String[] { Commons.admin }, MaapiUserSessionFlag.PROTO_TCP);

            int userSessId = m.getUserSessions()[0];
            String userName = m.getUserSession(userSessId).getUser();
            String password = authgroupPassword(ncsRoot, userName, authGroup);

            // double quotes are required for day0 varaibles values
            password = '"' + password + '"';
            userName = '"' + userName + '"';

            vars.putQuoted(Commons.tenant, tenant);
            vars.putQuoted(Commons.esc, esc);
            vars.putQuoted(Commons.depName, csrName);
            vars.put(Commons.userName, userName);
            vars.put(Commons.password, password);
            vars.putQuoted(Commons.authGroup, authGroup);
            vars.putQuoted(Commons.mgmtNetworkName, mgmtNetworkName);
            vars.putQuoted(Commons.egressNet, egressNet);
            vars.putQuoted(Commons.ingressNet, ingressNet);
            vars.putQuoted(Commons.mgmtIp, Utility.getIPAddress(mgmtIP));
            vars.putQuoted(Commons.vnfPhysicalNetwork, vnfPhysicalNetwork);
            vars.putQuoted(Commons.egressNetmask, Utility.getNetMask(wanIp));
            vars.putQuoted(Commons.ingressNetmask, Utility.getNetMask(lanIp));
            vars.putQuoted(Commons.egressNetwork, Utility.getNetAddr(wanIp) + wanMask);
            vars.putQuoted(Commons.ingressNetwork, Utility.getNetAddr(lanIp) + lanMask);
            vars.putQuoted(Commons.egressVlanId, String.valueOf(egressVlanId.longValue()));
            vars.putQuoted(Commons.ingressVlanId, String.valueOf(ingressVlanId.longValue()));
            vars.putQuoted(Commons.lanMacAddress, ingressMacAddress);
            vars.putQuoted(Commons.wanMacAddress, egressMacAddress);
            vars.putQuoted(Commons.lanIpAddress, lanValue);
            vars.putQuoted(Commons.wanIpAddress, wanValue);
            createCsrVmTemplate.apply(service, vars);
            LOGGER.info("Applied nfvo template");
        } finally {
            Helper.safeclose(m);
        }
    }

	// Set DCI link data of Site to service instance Topology
	public void addDciToTopology(NavuContainer netCon, NavuContainer link, long VlanID, String dciName)
			throws ConfException, NavuException {
		String ifType = link.leaf("if-type-in").valueAsString();
		String ifName = link.leaf("if-name-in").valueAsString() + "." + VlanID;
		String remDevName = "DCN";
		String remIfType = link.leaf("if-type-out").valueAsString();
		String remIfName = link.leaf("if-name-out").valueAsString();
		String[] key = { dciName, ifType, ifName };
		NavuContainer newCon = netCon.list(Commons.dciPort).sharedCreate(key);
		if (remDevName != null) {
			newCon.leaf(Commons.remoteDevice).sharedSet(remDevName);
			if (remIfName != null) {
				newCon.leaf(Commons.remoteIfType).sharedSet(remIfType);
				newCon.leaf(Commons.remoteIfName).sharedSet(remIfName + "." + VlanID);
			}
		}
	}

	// Set CSR network data to service instance topology
	public void addCsrToTopology(NavuContainer netCon, String CsrName, String ifType, String ifName, String ipAddress)
			throws ConfException, NavuException {
		NavuContainer newCon = netCon.list(Commons.csrPort).sharedCreate(CsrName);
		newCon.leaf(Commons.ifType).sharedSet(ifType);
		newCon.leaf(Commons.ifName).sharedSet(ifName);
		newCon.leaf(Commons.ipAddress).sharedSet(ipAddress);
	}

	private void applyDciTemplate(NavuNode service, ServiceContext context, String device, String ifNameIn,
			String ifNameOut, String ifTypeOut, String egressVlanId, String ingressVlanId) throws ConfException {
		LOGGER.info("Applying DCI config - starts");
		TemplateVariables variablesDci = new TemplateVariables();
		Template templateDci = new Template(context, Commons.dciTemplate);
		variablesDci.putQuoted("DEVICE", device);
		variablesDci.putQuoted("IF_NAME_IN", ifNameIn);
		variablesDci.putQuoted("IF_NAME_OUT", ifNameOut);
		LOGGER.info("IF TYPE OUT:" + ifNameOut);
		if (ifTypeOut.contains("Bundle")) {
			variablesDci.putQuoted("IS_BUNDLE", "true");
			LOGGER.info("infterface out type is Bundle-Ether");
			variablesDci.putQuoted("IF_TYPE_OUT", "Bundle-Ether");
		}
		if (ifTypeOut.contains("Gig")) {
			variablesDci.putQuoted("IS_BUNDLE", "false");
			LOGGER.info("infterface out type is GigabitEthernet");
			variablesDci.putQuoted("IF_TYPE_OUT", "GigabitEthernet");
		}
		variablesDci.putQuoted("EGRESS_VLANID", egressVlanId);
		variablesDci.putQuoted("INGRESS_VLANID", ingressVlanId);
		templateDci.apply(service, variablesDci);
		LOGGER.info("Applying DCI config - end");
	}

	// Change service deployment status
	// NOTE: This is test implementation for action
	private void changeDeployStatus(NavuNode service, String stat) throws ConfException, NavuException {
		NavuLeaf status = service.leaf(nfv._deploy_status);
		status.sharedSet(stat);
	}

	private boolean checkTenant(NavuNode ncsRoot, String csrName, String esc, String sharedTenant) {
		boolean DynamicTenant = true;
		NavuList tenantList;
		try {
			tenantList = ncsRoot.container("ncs", "devices").list("device").elem(esc).container("config")
					.container("esc", "esc_datamodel").container("tenants").list("tenant");

			for (NavuNode tenantElem : tenantList) {
				String tenantName = tenantElem.leaf("name").valueAsString();
				if (tenantName.equals(sharedTenant)) {
					LOGGER.info("Shared tenant name :  " + sharedTenant);
					NavuList depList = tenantElem.container("deployments").list("deployment");
					for (NavuNode depElem : depList) {
						LOGGER.info("Deployments under shared tenant: " + depElem.leaf("name").valueAsString());
						if (depElem.leaf("name").valueAsString().equals(csrName)) {
							LOGGER.info("Given service is with shared tenant: " + csrName);
							DynamicTenant = false;
							break;
						}
					}
					break;
				}

			}
		} catch (NavuException ne) {
			LOGGER.error("Exception while checking for dynamic Tenant :  " + ne.getMessage());
		} catch (Exception e) {
			LOGGER.error("Exception while checking for dynamic Tenant :  " + e.getMessage());
		}
		return DynamicTenant;

	}

	private void checkDeviceSync(ServiceContext context, String device) throws DpCallbackException {
		if (device != null) {
			LOGGER.info("start check device sync : " + device);
			try {
				Maapi maapi;
				maapi = context.getRootNode().context().getMaapi();
				ConfXMLParam[] deviceSync1 = maapi.requestAction(new ConfXMLParam[] {},
						"/ncs:devices/device{%x}/check-sync", device);
				String deviceStatus = deviceSync1[0].getValue().toString();
				LOGGER.info("check-sync for device," + device + " result : " + deviceStatus);
				if (!deviceStatus.contains("2")) {
					throw new DpCallbackException(
							String.format("Device %1$s is out of sync, " + "has sync-from been performed?", device));
				}
			} catch (DpCallbackException e) {
				e.printStackTrace();

				throw (DpCallbackException) e;
			} catch (Exception e) {
				e.printStackTrace();
				throw new DpCallbackException(Commons.notAbleToCheckDevices, e);
			}
			LOGGER.info("end check device sync : " + device);
		}
	}

	private void deregisterSmartLicense(String device, ServiceContext context) {
		try {
			Maapi maapi;
			maapi = context.getRootNode().context().getMaapi();
			LOGGER.info("Invoked deregister Smart License: " + device);
			ConfList list = new ConfList();
			list.addElem(new ConfBuf("smart"));
			list.addElem(new ConfBuf("deregister"));
			ConfXMLParam[] response = null;
			ConfXMLParam[] params = new ConfXMLParam[] { new ConfXMLParamValue("ios-stats", "args", list) };

			response = maapi.requestAction(params, "/ncs:devices/device{%s}/live-status/%s:exec/license", device,
					"ios-stats");
			LOGGER.info("Completed deregistration of Smart License: " + device);

		} catch (ConfException conf) {
			LOGGER.error("ConfException while deregistering smart license: " + conf.getMessage());
			conf.printStackTrace();
		} catch (IOException io) {
			LOGGER.error("IOException while deregistering smart license: " + io.getMessage());
			io.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Exception while deregistering smart license: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String authgroupPassword(NavuNode ncsRoot, String username, String authgrp)
			throws ConfException, IOException {
		NavuContainer ag = ncsRoot.container(Ncs._devices_).container(Ncs._authgroups_).list(Ncs._group_).elem(authgrp);

		String encPwd;
		if (ag.list(Ncs._umap_).containsNode(username)) {
			encPwd = ag.list(Ncs._umap_).elem(username).leaf(Ncs._remote_password_).valueAsString();
		} else {
			encPwd = ag.container(Ncs._default_map_).leaf(Ncs._remote_password_).valueAsString();
		}

		MaapiCrypto mc = new MaapiCrypto(ncsRoot.context().getMaapi());
		return mc.decrypt(encPwd);
	}

	/**
	 * Fetches the value based on key
	 */
	private String getValue(NavuNode node, String key) throws NavuException {
		return node.leaf(key).valueAsString();
	}

	/**
	 * Fetches the value based on key and also logs it
	 */
	private String getAndLogValue(NavuNode node, String key) throws NavuException {
		LOGGER.info(key + " : " + node.leaf(key).valueAsString());
		return node.leaf(key).valueAsString();
	}
}
