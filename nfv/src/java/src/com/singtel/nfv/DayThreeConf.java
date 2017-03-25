package com.singtel.nfv;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.singtel.nfv.namespaces.nfv;
import com.tailf.conf.ConfException;
import com.tailf.dp.DpCallbackException;
import com.tailf.dp.services.ServiceContext;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuException;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;
import com.tailf.ncs.template.Template;
import com.tailf.ncs.template.TemplateVariables;

public class DayThreeConf {
	private static Logger LOGGER = Logger.getLogger(DayThreeConf.class);
	ServiceContext context;
	NavuNode service;
	String csrName;

	DayThreeConf(ServiceContext context, NavuNode service, String csrName) {
		this.context = context;
		this.service = service;
		this.csrName = csrName;
	}

	public void applyConfiguration() throws NavuException, UnknownHostException, ConfException, Exception {
		LOGGER.info("Start day-3 configuration");
		drop3_Keychainloopbackconfig();
		extAclConfig();
		trackObjectConfig();
		ospfConfig();
		eigrpconfig();
		hsrpconfig();
		vrrpConfig();
		qosConfig();
		prefixListConf();
		bgpConfig();
		LOGGER.info("End day-3 configuration");
	}

	public void drop3_Keychainloopbackconfig() throws ConfException, NavuException, UnknownHostException, Exception {
		LOGGER.info("start key chain configuration");
		Template drop3Commontemp = new Template(context, Commons.drop3CommonTemmplate);
		TemplateVariables vars = new TemplateVariables();
		String interfaceId = "";
		String interfaceIp = "";
		String interfaceMask = "";
		String primaryIp = "";
		String primaryMask = "";
		boolean applyTemplate = false;
		int aclRuleCounter = 0;
		String aclInterfaceIp = "";
		String aclInterfaceMask = "";
		int routeMapCounter = 0;
		int ruleCounter = 0;
		int aclCounter = 0;
		int interfaceOutercounter = 0;
		int interfaceInnercounter = 0;
		int interfSecIpAddcounter = 0;

		vars.putQuoted("device", csrName);
		vars.putQuoted("interfaceId", interfaceId);
		vars.putQuoted("interfaceIp", interfaceIp);
		vars.putQuoted("interfaceMask", interfaceMask);
		vars.putQuoted("primaryIp", primaryIp);
		vars.putQuoted("primaryMask", primaryMask);
		vars.putQuoted("routeMapCounter", String.valueOf(routeMapCounter));
		vars.putQuoted("ruleCounter", String.valueOf(ruleCounter));
		vars.putQuoted("interfaceOutercounter", String.valueOf(interfaceOutercounter));
		vars.putQuoted("interfaceInnercounter", String.valueOf(interfaceInnercounter));
		vars.putQuoted("interfSecIpAddcounter", String.valueOf(interfSecIpAddcounter));
		vars.putQuoted("aclInterfaceIp", aclInterfaceIp);
		vars.putQuoted("aclInterfaceMask", aclInterfaceMask);
		vars.putQuoted("aclRuleCounter", String.valueOf(aclRuleCounter));
		vars.putQuoted("aclCounter", String.valueOf(aclCounter));
		vars.putQuoted("seqNo", "");
		vars.putQuoted("action", "");

		for (NavuNode aclNode : service.container("access-list").container("standard").list("std-named-acl")) {
			aclCounter++;
			aclRuleCounter = 0;
			applyTemplate = false;
			vars.putQuoted("device", csrName);
			vars.putQuoted("aclCounter", String.valueOf(aclCounter));
			for (NavuNode ruleNode : aclNode.list("rule")) {
				aclRuleCounter++;
				vars.putQuoted("aclRuleCounter", String.valueOf(aclRuleCounter));
				String source = ruleNode.leaf("source").valueAsString();

				if (source != null) {
					if (source.contains("/")) {
						aclInterfaceIp = Utility.getIPAddress(source);
						aclInterfaceMask = Utility.getNetMask(source);
						aclInterfaceMask = Utility.getWildCardMask(aclInterfaceMask);
						vars.putQuoted("seqNo", ruleNode.leaf("seq-no").valueAsString());
						vars.putQuoted("action", ruleNode.leaf("action").valueAsString());
						vars.putQuoted("aclInterfaceIp", aclInterfaceIp);
						vars.putQuoted("aclInterfaceMask", aclInterfaceMask);
					}
					drop3Commontemp.apply(service, vars);
					applyTemplate = true;
				}
			}
			if (!applyTemplate) {
				drop3Commontemp.apply(service, vars);
				applyTemplate = true;
			}
		}

		if (!applyTemplate)
			drop3Commontemp.apply(service, vars);

		NavuList routeMap = service.list("route-map");
		if (routeMap != null) {
			for (NavuNode routeMapList : routeMap) {
				routeMapCounter++;
				ruleCounter = 0;
				vars.putQuoted("routeMapCounter", String.valueOf(routeMapCounter));
				for (NavuNode ruleList : routeMapList.list("rule"))
					if (ruleList != null) {
						ruleCounter++;
						vars.putQuoted("ruleCounter", String.valueOf(ruleCounter));
						drop3Commontemp.apply(service, vars);
					}
			}
		}

		for (NavuNode interFace : service.list("interface")) {
			interfaceOutercounter++;
			interfaceInnercounter = 0;
			interfSecIpAddcounter = 0;
			interfaceId = interFace.leaf("interface-id").valueAsString();
			vars.putQuoted("interfaceId", interfaceId);
			vars.putQuoted("interfaceOutercounter", String.valueOf(interfaceOutercounter));
			vars.putQuoted("interfaceIp", "");
			vars.putQuoted("interfaceMask", "");

			for (NavuNode secondaryAddress : interFace.list("secondary-ip")) {
				interfSecIpAddcounter++;
				String secondaryIp = secondaryAddress.leaf("address").valueAsString();
				interfaceIp = Utility.getIPAddress(secondaryIp);
				interfaceMask = Utility.getNetMask(secondaryIp);
				vars.putQuoted("interfaceIp", interfaceIp);
				vars.putQuoted("interfaceMask", interfaceMask);
				vars.putQuoted("interfSecIpAddcounter", String.valueOf(interfSecIpAddcounter));
				drop3Commontemp.apply(service, vars);
			}
			for (NavuNode eigrp : interFace.list("eigrp")) {
				interfaceInnercounter++;
				vars.putQuoted("interfaceInnercounter", String.valueOf(interfaceInnercounter));
				drop3Commontemp.apply(service, vars);
			}
			drop3Commontemp.apply(service, vars);
		}
		LOGGER.info("end key chain configuration");
	}

	public void eigrpconfig() throws ConfException, UnknownHostException, Exception {
		LOGGER.info("start eigrp configuration");
		Template eigrptemp = new Template(context, Commons.eigrpTemplate);
		TemplateVariables vars = new TemplateVariables();
		String ipaddress = "";
		String mask = "";
		int eigrpCounter = 0;

		vars.putQuoted("device", csrName);
		vars.putQuoted("IPADDRESS", ipaddress);
		vars.putQuoted("MASK", mask);
		vars.putQuoted("eigrpCounter", String.valueOf(eigrpCounter));

		NavuList eigrpList = service.list("eigrp");
		for (NavuNode eigrpNode : eigrpList) {
			eigrpCounter++;

			if (eigrpNode.container("redistribute").container("bgp").leaf("as-number").exists()) {
				String serviceAsNumber = service.leaf("as-number").valueAsString();
				String eigrpBgpAsNumber = eigrpNode.container("redistribute").container("bgp").leaf("as-number")
						.valueAsString();
				if (eigrpBgpAsNumber != null && eigrpBgpAsNumber.length() != 0) {
					if (serviceAsNumber != null && serviceAsNumber.length() != 0) {
						if (!serviceAsNumber.equalsIgnoreCase(eigrpBgpAsNumber)) {
							throw new DpCallbackException(String.format(
									": EIGRP, please provide the AS number which is already configured :  %1$s ",
									serviceAsNumber));
						}
					}
				}
			}
			vars.putQuoted("eigrpCounter", String.valueOf(eigrpCounter));
			for (NavuNode nwtNode : eigrpNode.list("network")) {
				String ipAddress = nwtNode.leaf("ip-address").valueAsString();
				ipaddress = Utility.getIPAddress(ipAddress);
				mask = Utility.getNetMask(ipAddress);
				mask = Utility.getWildCardMask(mask);
				vars.putQuoted("IPADDRESS", ipaddress);
				vars.putQuoted("MASK", mask);
				eigrptemp.apply(service, vars);
			}
			eigrptemp.apply(service, vars);
		}
		LOGGER.info("end eigrp configuration");
	}

	public void ospfConfig() throws ConfException, UnknownHostException {
		LOGGER.info("start ospf configuration");
		Template ospfTemplate = new Template(context, Commons.ospfTemplate);
		TemplateVariables vars = new TemplateVariables();

		String ospfNetIpAddress = "";
		String ospfNetIpMask = "";
		String ospfAreaId = "";
		int ospfCounter = 0;

		vars.putQuoted("device", csrName);
		vars.putQuoted("ospfCounter", String.valueOf(ospfCounter));
		vars.putQuoted("ospfNetIpAddress", ospfNetIpAddress);
		vars.putQuoted("ospfNetIpMask", ospfNetIpMask);
		vars.putQuoted("ospfAreaId", ospfAreaId);

		for (NavuNode ospfNode : service.list("ospf")) {
			ospfCounter++;
			if (ospfNode.container("redistribute").container("bgp").leaf("as-number").exists()) {
				String serviceAsNumber = service.leaf("as-number").valueAsString();
				String ospfBgpAsNumber = ospfNode.container("redistribute").container("bgp").leaf("as-number")
						.valueAsString();
				if (ospfBgpAsNumber != null && ospfBgpAsNumber.length() != 0) {
					if (serviceAsNumber != null && serviceAsNumber.length() != 0) {
						if (!serviceAsNumber.equalsIgnoreCase(ospfBgpAsNumber)) {
							throw new DpCallbackException(String.format(
									": OSPF, please provide the AS number which is already configured :  %1$s ",
									serviceAsNumber));
						}
					}
				}
			}

			vars.putQuoted("ospfCounter", String.valueOf(ospfCounter));
			for (NavuNode nwtNode : ospfNode.list("network")) {
				String netIpAddressWithPrefix = nwtNode.leaf("ip-address").valueAsString();
				ospfNetIpAddress = Utility.getIPAddress(netIpAddressWithPrefix);
				ospfNetIpMask = Utility.getNetMask(netIpAddressWithPrefix);
				ospfAreaId = getValue(nwtNode, nfv._area_id_);
				vars.putQuoted("ospfNetIpAddress", ospfNetIpAddress);
				vars.putQuoted("ospfNetIpMask", ospfNetIpMask);
				vars.putQuoted("ospfAreaId", ospfAreaId);
				ospfTemplate.apply(service, vars);
			}
			ospfTemplate.apply(service, vars);
		}
		LOGGER.info("end ospf configuration");
	}

	public void hsrpconfig() throws ConfException {
		LOGGER.info("end hsrp configuration");
		int hsrpOuterConter=0;
		int hsrpInnerConter=0;
		int trackCounter = 0;
		Template hsrpTemplate = new Template(context, Commons.hsrpTemplate);
		TemplateVariables vars = new TemplateVariables();
		
		vars.putQuoted("device", csrName);
		vars.putQuoted("hsrpOuterConter", String.valueOf(hsrpOuterConter));
		vars.putQuoted("hsrpInnerConter", String.valueOf(hsrpInnerConter));
		vars.putQuoted("trackCounter", String.valueOf(trackCounter));
		
		for (NavuNode interFace : service.list("interface")) {
			hsrpOuterConter++;
			hsrpInnerConter=0;
			vars.putQuoted("hsrpOuterConter", String.valueOf(hsrpOuterConter));
		for (NavuNode hsrp : interFace.container("hsrp").list("hsrp-group")) {
			hsrpInnerConter ++;
			trackCounter = 0;
			vars.putQuoted("hsrpInnerConter", String.valueOf(hsrpInnerConter));
			for (NavuNode trackNode : hsrp.list("track")) {
				trackCounter++;
				vars.putQuoted("trackCounter", String.valueOf(trackCounter));
				hsrpTemplate.apply(service, vars);
			}
			hsrpTemplate.apply(service, vars);
		   }
		}
		
		LOGGER.info("end hsrp configuration");
	}

	public void vrrpConfig() throws ConfException {
		LOGGER.info("start vrrp configuration");
		Template vrrpTemplate = new Template(context, Commons.vrrpTemplate);
		TemplateVariables vars = new TemplateVariables();
		int intfCounter = 0;
		int vrrpCounter = 0;
		int trackCounter = 0;

		vars.putQuoted("device", csrName);
		vars.putQuoted("intfCounter", String.valueOf(intfCounter));
		vars.putQuoted("vrrpCounter", String.valueOf(vrrpCounter));
		vars.putQuoted("trackCounter", String.valueOf(trackCounter));

		for (NavuNode intfNode : service.list(nfv._interface_)) {
			intfCounter++;
			vrrpCounter = 0;
			vars.putQuoted("intfCounter", String.valueOf(intfCounter));
			for (NavuNode vrrpNode : intfNode.container(nfv._vrrp_).list(nfv._vrrp_group_)) {
				vrrpCounter++;
				trackCounter = 0;
				vars.putQuoted("vrrpCounter", String.valueOf(vrrpCounter));
				for (NavuNode trackNode : vrrpNode.list(nfv._track_)) {
					trackCounter++;
					vars.putQuoted("trackCounter", String.valueOf(trackCounter));
					vrrpTemplate.apply(service, vars);
				}
				vrrpTemplate.apply(service, vars);
			}
		}
		LOGGER.info("end vrrp configuration");
	}

	// ********************** prefix lists - starts ****************/

	public void prefixListConf() throws ConfException, NavuException {
		LOGGER.info("start Prefix list");
		int prefixListCounter = 0;
		Template prefixListTemplate = new Template(context, Commons.prefixListTemplate);
		NavuList prefixList = service.list("prefix-list");
		for (NavuNode prefixEntry : prefixList) {
			prefixListCounter++;
			TemplateVariables vars = new TemplateVariables();
			vars.putQuoted(Commons.device, csrName);
			vars.putQuoted("prefixListCounter", String.valueOf(prefixListCounter));
			NavuList rulesList = prefixEntry.list("rule");
			int ruleCounter = 0;
			for (NavuNode rule : rulesList) {
				ruleCounter++;
				vars.putQuoted("ruleCounter", String.valueOf(ruleCounter));
				prefixListTemplate.apply(service, vars);
			}
		}
		LOGGER.info("end prefix list");
	}
	// ******************* Prefix List - end *******************/

	// ******************* BGP ROUTING - STARTS *******************/
	public void bgpConfig() throws ConfException, UnknownHostException {
		LOGGER.info("start BGP configuration");
		Template bgpTemplate = new Template(context, Commons.bgpTemplate);
		TemplateVariables vars = new TemplateVariables();
		String asNumber = service.leaf(nfv._as_number).valueAsString();
		NavuList bgpList = service.list(nfv._bgp);
		String bgpNetwork = "";
		String bgpNetmask = "";
		String restartTime = "";
		String staleTime = "";
		int neighborCounter = 0;
		int prefixCounter = 0;
		int rootmapCounter = 0;
		int addressFamilyCounter = 0;
		int addressNbCounter = 0;
		int addressRouteCounter = 0;
		vars.putQuoted("BGP_NETWORK_IP", bgpNetwork);
		vars.putQuoted("BGP_NETWORK_MASK", bgpNetmask);
		vars.putQuoted("RESTART_TIME", restartTime);
		vars.putQuoted("STALE_PATH_TIME", staleTime);
		vars.putQuoted("neighborCounter", String.valueOf(neighborCounter));
		vars.putQuoted("prefixCounter", String.valueOf(prefixCounter));
		vars.putQuoted("rootmapCounter", String.valueOf(rootmapCounter));
		vars.putQuoted("addressFamilyCounter", String.valueOf(addressFamilyCounter));
		vars.putQuoted("addressNbCounter", String.valueOf(addressNbCounter));
		vars.putQuoted("addressRouteCounter", String.valueOf(addressRouteCounter));
		for (NavuNode bgp : bgpList) {
			String bgpAsNumber = bgp.leaf("as-number").valueAsString();
			LOGGER.info("as number from service creation: " + asNumber);
			LOGGER.info("as number from new bgp list: " + bgpAsNumber);
			if (asNumber != null && !asNumber.equalsIgnoreCase(bgpAsNumber)) {
				throw new DpCallbackException(
						String.format(": Please provide the AS number which is already configured :  %1$s ", asNumber));
			}
			vars.putQuoted("device", csrName);
			NavuList bgpNetworkList = bgp.list("network");
			for (NavuNode netWorkEle : bgpNetworkList) {
				bgpNetwork = netWorkEle.leaf("ip-address").valueAsString();
				LOGGER.info("BGP network: " + bgpNetwork);
				if (bgpNetwork != null) {
					bgpNetmask = Utility.getNetMask(bgpNetwork);
					bgpNetwork = Utility.getNetAddr(bgpNetwork);
				}
				vars.putQuoted("BGP_NETWORK_IP", bgpNetwork);
				vars.putQuoted("BGP_NETWORK_MASK", bgpNetmask);
				bgpTemplate.apply(service, vars);
			}

			NavuList neighborList = bgp.list("neighbor");
			neighborCounter = 0;
			for (NavuContainer neighbor : neighborList) {
				neighborCounter++;
				vars.putQuoted("neighborCounter", String.valueOf(neighborCounter));
				bgpTemplate.apply(service, vars);
			}
			NavuList addressList = bgp.list("address-family");
			addressFamilyCounter = 0;
			for (NavuContainer addressElem : addressList) {
				addressFamilyCounter++;
				vars.putQuoted("addressFamilyCounter", String.valueOf(addressFamilyCounter));
				NavuList addressNbList = addressElem.list("neighbor");
				addressNbCounter = 0;
				for (NavuContainer addressNeighbor : addressNbList) {
					addressNbCounter++;
					vars.putQuoted("addressNbCounter", String.valueOf(addressNbCounter));
					NavuList prefixList = addressNeighbor.list("prefix-list");
					prefixCounter = 0;
					for (NavuContainer prefixElem : prefixList) {
						prefixCounter++;
						vars.putQuoted("prefixCounter", String.valueOf(prefixCounter));
						bgpTemplate.apply(service, vars);
					}
					NavuList addressRouteList = addressNeighbor.list("route-map");
					addressRouteCounter = 0;
					for (NavuContainer addressRouteMap : addressRouteList) {
						addressRouteCounter++;
						vars.putQuoted("addressRouteCounter", String.valueOf(addressRouteCounter));
						bgpTemplate.apply(service, vars);
					}
					bgpTemplate.apply(service, vars);

				}
				bgpTemplate.apply(service, vars);

			}
			NavuContainer restartContainer = bgp.container("graceful-restart");

			restartTime = restartContainer.leaf("restart-time").valueAsString();
			LOGGER.info("BGP restart time: " + restartTime);

			staleTime = restartContainer.leaf("stale-path-time").valueAsString();
			LOGGER.info("BGP stale time: " + staleTime);
			if (restartTime != null)
				vars.putQuoted("RESTART_TIME", restartTime);
			else
				vars.putQuoted("RESTART_TIME", "");
			if (staleTime != null)
				vars.putQuoted("STALE_PATH_TIME", staleTime);
			else
				vars.putQuoted("STALE_PATH_TIME", "");
			bgpTemplate.apply(service, vars);
		}
		LOGGER.info("end BGP configuration");
	}
	// ******************* BGP ROUTING - END *******************/

	/**
	 * configures the following access-list in or out and access-group service
	 * policy input or output and service-policy Qos
	 */
	public void qosConfig() throws ConfException {
		LOGGER.info("start qos configuring");
		Template qosTemplate = new Template(context, Commons.qosTemplate);
		TemplateVariables vars = new TemplateVariables();
		int classMapCounter = 0;
		int policyMapCounter = 0;
		int iterateCounter = 0;
		int intfCounter = 0;
		int aclCounter = 0;
		int servicePolicyCounter = 0;

		vars.putQuoted("device", csrName);
		vars.putQuoted("intfCounter", String.valueOf(intfCounter));
		vars.putQuoted("aclCounter", String.valueOf(aclCounter));
		vars.putQuoted("servicePolicyCounter", String.valueOf(servicePolicyCounter));

		for (NavuNode intfNode : service.list("interface")) {
			intfCounter++;
			aclCounter = 0;
			servicePolicyCounter = 0;
			vars.putQuoted("intfCounter", String.valueOf(intfCounter));

			for (NavuNode aclNode : intfNode.list("access-list")) {
				aclCounter++;
				vars.putQuoted("aclCounter", String.valueOf(aclCounter));
				qosTemplate.apply(service, vars);
			}
			for (NavuNode servicePolicyNode : intfNode.list("service-policy")) {
				servicePolicyCounter++;
				vars.putQuoted("servicePolicyCounter", String.valueOf(servicePolicyCounter));
				qosTemplate.apply(service, vars);
			}
		}

		if (service.container("qos").exists()) {
			classMapCounter = service.container("qos").list("class-map").size();
			policyMapCounter = service.container("qos").list("policy-map").size();
			iterateCounter = (classMapCounter > policyMapCounter) ? classMapCounter : policyMapCounter;

			while (iterateCounter > 0) {
				qosTemplate.apply(service, vars);
				iterateCounter--;
			}
		}
		LOGGER.info("end qos configuring");
	}

	/**
	 * configures service access-list extended ext-named-acl Note: there is also
	 * access-list under service interface.
	 */
	public void extAclConfig() throws Exception {
		LOGGER.info("start extended access-list configuring");
		Template extAclListTemplate = new Template(context, Commons.extAclListTemplate);
		TemplateVariables vars = new TemplateVariables();
		boolean applyTemplate;

		for (NavuNode extNamedAclNode : service.container("access-list").container("extended").list("ext-named-acl")) {
			String aclRule = "";
			String protocol = "";
			applyTemplate = false;
			String extendedAclName = getValue(extNamedAclNode, "name");
			String extendedAclEnable = getValue(extNamedAclNode, "admin-state");

			vars.putQuoted("device", csrName);
			vars.putQuoted("aclRule", aclRule);
			vars.putQuoted("extendedAclName", extendedAclName);
			vars.putQuoted("extendedAclEnable", extendedAclEnable);

			for (NavuNode ruleNode : extNamedAclNode.list("rule")) {
				String seqNo = "";
				String action = "";
				String source = "";
				String icmpMsg = "";
				String icmpType = "";
				String icmpCode = "";
				String sourcePort = "";
				String destination = "";
				String sourcePortOp = "";
				String sourcePortLast = "";
				String destinationPort = "";
				String destinationPortOp = "";
				String destinationPortLast = "";
				boolean isProtocolAllowed = false;

				protocol = getValue(ruleNode, "protocol");

				if ("ip".equals(protocol)) {
					protocol = "ip";
					isProtocolAllowed = true;
				} else if ("1".equals(protocol) || "icmp".equals(protocol)) {
					protocol = "icmp";
					isProtocolAllowed = true;
				} else if ("2".equals(protocol) || "igmp".equals(protocol)) {
					protocol = "igmp";
					isProtocolAllowed = true;
				} else if ("4".equals(protocol) || "ipinip".equals(protocol)) {
					protocol = "ipinip";
					isProtocolAllowed = true;
				} else if ("6".equals(protocol) || "tcp".equals(protocol)) {
					protocol = "tcp";
					isProtocolAllowed = true;
				} else if ("17".equals(protocol) || "udp".equals(protocol)) {
					protocol = "udp";
					isProtocolAllowed = true;
				} else if ("47".equals(protocol) || "gre".equals(protocol)) {
					protocol = "gre";
					isProtocolAllowed = true;
				} else if ("50".equals(protocol) || "esp".equals(protocol)) {
					protocol = "esp";
					isProtocolAllowed = true;
				} else if ("51".equals(protocol) || "ahp".equals(protocol)) {
					protocol = "ahp";
					isProtocolAllowed = true;
				} else if ("88".equals(protocol) || "eigrp".equals(protocol)) {
					protocol = "eigrp";
					isProtocolAllowed = true;
				} else if ("89".equals(protocol) || "ospf".equals(protocol)) {
					protocol = "ospf";
					isProtocolAllowed = true;
				} else if ("94".equals(protocol) || "nos".equals(protocol)) {
					protocol = "nos";
					isProtocolAllowed = true;
				} else if ("103".equals(protocol) || "pim".equals(protocol)) {
					protocol = "pim";
					isProtocolAllowed = true;
				} else if ("108".equals(protocol) || "pcp".equals(protocol)) {
					protocol = "pcp";
					isProtocolAllowed = true;
				}
				// end of if clause

				if (isProtocolAllowed) {
					seqNo = getValue(ruleNode, "seq-no");
					action = getValue(ruleNode, "action");
					source = getValue(ruleNode, "source");
					destination = getValue(ruleNode, "destination");

					if ("tcp".equals(protocol) || "udp".equals(protocol)) {
						sourcePort = getValue(ruleNode, "source-port");
						sourcePortOp = getValue(ruleNode, "source-port-op");
						sourcePortLast = getValue(ruleNode, "source-port-last");

						destinationPort = getValue(ruleNode, "destination-port");
						destinationPortOp = getValue(ruleNode, "destination-port-op");
						destinationPortLast = getValue(ruleNode, "destination-port-last");
					}

					if ("icmp".equals(protocol)) {
						icmpType = getValue(ruleNode.container("icmp"), "type");
						icmpCode = getValue(ruleNode.container("icmp"), "code");
						icmpMsg = getValue(ruleNode.container("icmp"), "message");
					}

					if (source.length() != 0) {
						if (source.contains("/")) {
							source = Utility.getIPAddress(source) + " "
									+ Utility.getWildCardMask(Utility.getNetMask(source));
						} else {
							source = (source.equals("any")) ? source : "host " + source;
						}
					}

					if (destination.length() != 0) {
						if (destination.contains("/")) {
							destination = Utility.getIPAddress(destination) + " "
									+ Utility.getWildCardMask(Utility.getNetMask(destination));
						} else {
							destination = (destination.equals("any")) ? destination : "host " + destination;
						}
					}

					aclRule = seqNo + " " + action + " " + protocol + " " + source + " " + sourcePortOp + " "
							+ sourcePort + " " + sourcePortLast + " " + destination + " " + destinationPortOp + " "
							+ destinationPort + " " + destinationPortLast + " " + icmpType + " " + icmpCode;

					aclRule = aclRule.replaceAll(" +", " "); // replace 2 or
																// more spaces
																// with one
																// space
					aclRule = aclRule.trim() + " " + icmpMsg;
					vars.putQuoted("aclRule", aclRule.trim());
					extAclListTemplate.apply(service, vars);
					applyTemplate = true;
				}
			}
			if (!applyTemplate)
				extAclListTemplate.apply(service, vars);
		}
		LOGGER.info("end extended access-list configuring");
	}

	public String getValue(NavuNode node, String leafName) throws NavuException {
		if (node.leaf(leafName).exists()) {
			String value = node.leaf(leafName).valueAsString();
			if (value != null & value.length() != 0) {
				if ("none".equals(value)) // if source or destination operator
											// is none then return ""
				{
					return "";
				}
				return value;
			}
		}
		return "";
	}

	public void trackObjectConfig() throws ConfException, UnknownHostException {
		LOGGER.info("start track object configuration");
		Template trackObjectTemplate = new Template(context, Commons.trackObjectTemplate);
		TemplateVariables vars = new TemplateVariables();
		int trackObjectCounter = 0;
		int objectCounter = 0;
		boolean applyTemplate = false;
		String networkIpAddress = "";
		String networkIpMask = "";

		vars.putQuoted("device", csrName);
		vars.putQuoted("objectCounter", String.valueOf(objectCounter));
		vars.putQuoted("trackObjectCounter", String.valueOf(trackObjectCounter));
		vars.putQuoted("networkIpMask", networkIpMask);
		vars.putQuoted("networkIpAddress", networkIpAddress);

		for (NavuNode trackObjectNode : service.container("track").list("track-object")) {
			trackObjectCounter++;
			objectCounter = 0;
			applyTemplate = false;
			networkIpAddress = "";
			networkIpMask = "";
			vars.putQuoted("trackObjectCounter", String.valueOf(trackObjectCounter));
			vars.putQuoted("objectCounter", String.valueOf(objectCounter));
			String networkIp = trackObjectNode.container("ip-route").leaf("network-ip").valueAsString();
			LOGGER.info("networkIp =" + networkIp);
			if (networkIp != null && networkIp.contains("/")) {
				networkIpAddress = Utility.getIPAddress(networkIp);
				networkIpMask = Utility.getNetMask(networkIp);
				vars.putQuoted("networkIpAddress", networkIpAddress);
				vars.putQuoted("networkIpMask", networkIpMask);
			}
			for (NavuNode objectNode : trackObjectNode.container("list").list("object")) {
				objectCounter++;
				vars.putQuoted("objectCounter", String.valueOf(objectCounter));
				trackObjectTemplate.apply(service, vars);
				applyTemplate = true;
			}
			if (!applyTemplate)
				trackObjectTemplate.apply(service, vars);
		}
		LOGGER.info("end track object configuration");
	}
}