package com.singtel.nfv;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.singtel.nfv.namespaces.nfv;
import com.tailf.conf.ConfException;
import com.tailf.dp.services.ServiceContext;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuException;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;
import com.tailf.ncs.template.Template;
import com.tailf.ncs.template.TemplateVariables;

public class DayTwoConf {
	private static Logger LOGGER = Logger.getLogger(DayTwoConf.class);

	ServiceContext context;
	NavuNode service;
	String csrName;

	DayTwoConf(ServiceContext context, NavuNode service, String csrName) {
		this.context = context;
		this.service = service;
		this.csrName = csrName;
	}

	// apply all day two configuration
	public void applyConfiguration() throws Exception {
		LOGGER.info("Start day-2 configuration");
		dhcpConf();
		bgpPrefixConf();
		staticRoutesConfig();
		natConfig();
		patConf();
		confAcl();
		LOGGER.info("End day-2 configuration");
	}

	// ******************* WAN Routing BGP Prefix List *******************/
	public void bgpPrefixConf() throws ConfException, NavuException {
		LOGGER.info("start BGP prefix list");
		Template bgpPrefixListTemplate = new Template(context, Commons.bgpPrefixListTemplate);
		NavuList bgpList = service.list(nfv._bgp_prefix_list);
		for (NavuContainer bgp : bgpList) {

			String bgpDirection = bgp.leaf(nfv._bgp_direction).valueAsString();
			String bgpSeqNo = bgp.leaf(nfv._bgp_seq_no).valueAsString();
			LOGGER.info("BGP prefix list- bgp direction: " + bgpDirection);
			LOGGER.info("BGP prefix list- bgp seq number: " + bgpSeqNo);
			if (bgpDirection != null && bgpSeqNo != null) {
				TemplateVariables vars = new TemplateVariables();
				vars.putQuoted("device", csrName);

				// BGP IP Prefix List application
				vars.putQuoted("BGP_DIRECTION", bgpDirection);
				vars.putQuoted("BGP_SEQ_NO", bgpSeqNo);
				bgpPrefixListTemplate.apply(service, vars);
			}
		}
		LOGGER.info("end BGP prefix list");
	}

	// ******************* WAN Routing BGP Prefix List END *******************

	// ******************* LAN ROUTING static routes starts *******************
	public void staticRoutesConfig() throws ConfException, NavuException, UnknownHostException {
		LOGGER.info("start LAN ROUTING static routes configuration");
		NavuList staticList = service.list(nfv._static_routes);
		for (NavuContainer staticRouteList : staticList) {
			Template lanroutingTemplate = new Template(context, Commons.staticRouteListTemplate);
			String lanipnetwork = staticRouteList.leaf(nfv._lan_ip_network).valueAsString();
			String lannetmask = "";
			LOGGER.info("LAN routing static routes-lanipnetwork: " + lanipnetwork);
			if (lanipnetwork != null) {

				lannetmask = Utility.getNetMask(lanipnetwork);

				lanipnetwork = Utility.getIPAddress(lanipnetwork);
			} else
				lanipnetwork = "";

			String lannexthopip = staticRouteList.leaf(nfv._lan_next_hop_ip).valueAsString();
			String comment = staticRouteList.leaf(nfv._comment).valueAsString();
			if (comment == null)
				comment = "";
			LOGGER.info("LAN routing static routes-comment: " + comment);
			if (lanipnetwork != null && lannetmask != null && lannexthopip != null) {
				TemplateVariables vars = new TemplateVariables();
				vars.putQuoted("device", csrName);
				vars.putQuoted("maskroute", lannetmask);
				vars.putQuoted("prefixroute", lanipnetwork);
				vars.putQuoted("nicidgateway", lannexthopip);
				vars.putQuoted("routename", comment);
				lanroutingTemplate.apply(service, vars);
			}
		}
		LOGGER.info("end LAN ROUTING static routes configuration");
	}

	// ******************* LAN ROUTING static END *******************

	// ******************* NAT configuration starts *******************
	public void natConfig() throws ConfException, NavuException {
		LOGGER.info("start NAT configuration");
		Template natstaticlistTemplate = new Template(context, Commons.natStaticListTemplate);
		NavuContainer natcontainer = service.container(nfv._nat_param);
		NavuList natList = natcontainer.list(nfv._nat_list);

		for (NavuContainer statisNatList : natList) {
			String natSeqNo = statisNatList.leaf(nfv._nat_seq_no).valueAsString();
			LOGGER.info("NAT SEQ NUMBER" + natSeqNo);
			TemplateVariables vars = new TemplateVariables();
			vars.putQuoted(Commons.device, csrName);
			vars.putQuoted("NATSEQNO", natSeqNo);
			natstaticlistTemplate.apply(service, vars);
		}
		LOGGER.info("end NAT configuration");
	}

	// ******************* NAT configuration END *******************

	// ****************** LAN IP Assignment DHCP with Options *************

	public void dhcpConf() throws ConfException, UnknownHostException {
		LOGGER.info("start LAN IP Assignment DHCP with options configuration");
		String lanIp = service.leaf(nfv._lan_ip).valueAsString();
		Template dhcpTemplate = new Template(context, Commons.dhcpTemplate);
		NavuList dhcpList = service.list(nfv._dhcp_param);
		int dhcpOutercount = 0;
		int dhcpInnercount = 0;
		for (NavuNode dhcpElem : dhcpList) {
			String modifyParam = dhcpElem.leaf(nfv._modify_dhcp_param).valueAsString();
			String interfaceId = dhcpElem.leaf(nfv._interface_id).valueAsString();
			String lanNetwork = dhcpElem.leaf(nfv._lan_network).valueAsString();
			LOGGER.info("DHCP - modifyparam : " + modifyParam);
			LOGGER.info("DHCP - Interface ID : " + interfaceId);
			LOGGER.info("DHCP - LAN Network : " + lanNetwork);
			dhcpOutercount++;
			dhcpInnercount = 0;
			if (interfaceId == null)
				interfaceId = "";
			else{
				interfaceId = "Pool" + interfaceId;
				LOGGER.info("******************** Pool"+interfaceId);}
			if (modifyParam != null && modifyParam.equals("No")) {
				String DhcplanIp="";
				String mask="";
				
				if(interfaceId.substring(4).equals("3")){
					 DhcplanIp = Utility.getIPAddress(lanIp);
					 mask = Utility.getNetMask(lanIp);
				}
				else if(interfaceId.substring(4).startsWith("3.")){					
					String lanipAddress=null;
					for (NavuNode cVlan : service.list("lan-cvlans")) {
					if((interfaceId.substring(6)).equals((cVlan.leaf("lan-cvlan-id").valueAsString()))){
					lanipAddress = cVlan.leaf("lan-cvlan-ip").valueAsString();
						LOGGER.info("*******lanipAddress "+lanipAddress); 
					break;
					}
					}		
					if(lanipAddress !=null){
					 DhcplanIp = Utility.getIPAddress(lanipAddress);
					 mask = Utility.getNetMask(lanipAddress);	}						
				}
				else{
				LOGGER.info("Dhcp support only lan interface");
				}
				LOGGER.info("************************* DhcplanIp: " + DhcplanIp +"    "+mask);
				dhcpListNoCall(service, dhcpTemplate, csrName, DhcplanIp, mask, interfaceId,dhcpOutercount);
			} else {
				String dhcpLanNmask = "";
				String startaddress = "";
				String endaddress = "";

				if (lanNetwork != null) {
					dhcpLanNmask = Utility.getNetMask(lanNetwork);
					lanNetwork = Utility.getIPAddress(lanNetwork);
				} else{
					dhcpLanNmask = "";
					lanNetwork="";
				}
				NavuList excludeaddrs = dhcpElem.list("exclude-address");

				for (NavuNode excludeaddressElem : excludeaddrs) {
					startaddress = excludeaddressElem.leaf("start-address").valueAsString();
					endaddress = excludeaddressElem.leaf("end-address").valueAsString();
					if(endaddress==null)
					   endaddress="";
					LOGGER.info("start address" + startaddress);
					LOGGER.info("end address" + endaddress);
					dhcpListYesCall(service, dhcpTemplate, csrName, dhcpLanNmask, lanNetwork, interfaceId, startaddress,
							endaddress,dhcpOutercount);
				}
				if (startaddress.equals("") && endaddress.equals("")) {
					dhcpListYesCall(service, dhcpTemplate, csrName, dhcpLanNmask, lanNetwork, interfaceId, startaddress,
							endaddress,dhcpOutercount);
				}

			}
			if (dhcpElem.list("option") != null) {
				for (NavuNode option : dhcpElem.list("option")) {
					dhcpInnercount++;
					dhcpListOptionCall(service, dhcpTemplate, csrName, dhcpOutercount, dhcpInnercount);

				}
			}
		}
		LOGGER.info("end LAN IP Assignment DHCP with options configuration");
	}

	private void dhcpListNoCall(NavuNode service, Template dhcpTemplate, String csrName, String lan_ip_network,
			String mask, String interfaceId,int dhcpOutercount) throws ConfException {
		TemplateVariables vars = new TemplateVariables();
		vars.putQuoted("device", csrName);
		vars.putQuoted("lan-ip", lan_ip_network);
		vars.putQuoted("mask", mask);
		vars.putQuoted("Interface-Id", interfaceId);
		vars.putQuoted("dhcp-lan-netmask", " ");
		vars.putQuoted("dhcp-lan-network", " ");
		vars.putQuoted("startAddress", "");
		vars.putQuoted("endaddress", "");
		vars.putQuoted("dhcpOutercount", String.valueOf(dhcpOutercount));
		vars.putQuoted("dhcpInnercount", "");
		vars.putQuoted("option", "");
		dhcpTemplate.apply(service, vars);
	}

	private void dhcpListYesCall(NavuNode service, Template dhcpTemplate, String csrName, String dhcpLanNmask,
			String lanNetwork, String interfaceId, String startAddress, String endaddress,int dhcpOutercount) throws ConfException {
		TemplateVariables vars = new TemplateVariables();
		vars.putQuoted("device", csrName);
		vars.putQuoted("dhcp-lan-netmask", dhcpLanNmask);
		vars.putQuoted("dhcp-lan-network", lanNetwork);
		vars.putQuoted("Interface-Id", interfaceId);
		vars.putQuoted("lan-ip", " ");
		vars.putQuoted("mask", " ");
		vars.putQuoted("startAddress", startAddress);
		vars.putQuoted("endaddress", endaddress);
		vars.putQuoted("dhcpOutercount", String.valueOf(dhcpOutercount));
		vars.putQuoted("dhcpInnercount", "");
		vars.putQuoted("option", "");
		dhcpTemplate.apply(service, vars);
	}

	private void dhcpListOptionCall(NavuNode service, Template dhcpTemplate, String csrName, int dhcpOutercount,
			int dhcpInnercount) throws ConfException {
		TemplateVariables vars = new TemplateVariables();
		vars.putQuoted("device", csrName);
		vars.putQuoted("dhcp-lan-netmask", "");
		vars.putQuoted("dhcp-lan-network", "");
		vars.putQuoted("Interface-Id", "");
		vars.putQuoted("lan-ip", " ");
		vars.putQuoted("mask", " ");
		vars.putQuoted("startAddress", "");
		vars.putQuoted("endaddress", "");
		vars.putQuoted("dhcpOutercount", String.valueOf(dhcpOutercount));
		vars.putQuoted("dhcpInnercount", String.valueOf(dhcpInnercount));
		vars.putQuoted("option", "option");
		dhcpTemplate.apply(service, vars);
	}

	// ****************** LAN IP Assignment DHCP with Options END *************

	// ******************* PAT configuration starts *******************
	public void patConf() throws ConfException, UnknownHostException, Exception {
		LOGGER.info("start PAT configuration");
		Template patstaticlistTemplate = new Template(context, Commons.patStaticListTemplate);
		NavuContainer patcontainer = service.container(nfv._pat_param);
		NavuList patList = patcontainer.list(nfv._pat_list);
		for (NavuContainer staticPatList : patList) {
			String pat_action = staticPatList.leaf(nfv._pat_action).valueAsString();
			String pat_seq_no = staticPatList.leaf(nfv._pat_seq_no).valueAsString();
			String local_ip_any = staticPatList.leaf(nfv._local_ip_any_).valueAsString();
			LOGGER.info("PAT - pat action : " + pat_action);
			LOGGER.info("PAT - pat seq number : " + pat_seq_no);
			LOGGER.info("PAT - local ip any : " + local_ip_any);
			String local_ip_subnet = staticPatList.leaf(nfv._local_ip_subnet).valueAsString();
			LOGGER.info("PAT - local ip subnet : " + local_ip_subnet);

			String local_ip_wildcard_mask = "";

			if (local_ip_subnet != null) {
				local_ip_wildcard_mask = Utility.getNetMask(local_ip_subnet);
				local_ip_wildcard_mask = Utility.getWildCardMask(local_ip_wildcard_mask);
				local_ip_subnet = Utility.getIPAddress(local_ip_subnet);
			} else {
				local_ip_subnet = "";
			}
			if (local_ip_any != null && local_ip_any.equalsIgnoreCase(Commons.truee)) {
				local_ip_wildcard_mask = "255.255.255.255";
				local_ip_subnet = "0.0.0.0";
			}

			TemplateVariables vars = new TemplateVariables();
			vars.putQuoted(Commons.device, csrName);
			String rule = pat_seq_no + " " + pat_action + " " + local_ip_subnet + " " + local_ip_wildcard_mask;
			vars.putQuoted(Commons.rule, rule);
			patstaticlistTemplate.apply(service, vars);

		}
		LOGGER.info("end PAT configuration");
	}

	// ******************* PAT configuration END *******************

	// ******************* ACL configuration starts *******************
	public void confAcl() throws ConfException, UnknownHostException, Exception {
		LOGGER.info("start ACL configuration");
		Template aclListTemplate = new Template(context, Commons.aclListTemplate);
		NavuContainer aclcontainer = service.container(nfv._access_list);
		NavuList aclList = aclcontainer.list(nfv._access_list_members);
		for (NavuContainer accessList : aclList) {

			String access_list_name = "";
			String access_list_direction = accessList.leaf(nfv._acl_direction).valueAsString();
			String acl_interface_id = accessList.leaf(nfv._acl_interface_id).valueAsString();
			if (acl_interface_id!=null && access_list_direction != null)
			{
				access_list_name = "acl-"+acl_interface_id+"-"+access_list_direction;
			}


			String acl_seq_no = accessList.leaf(nfv._acl_seq_no).valueAsString();
			String acl_action = accessList.leaf(nfv._acl_action).valueAsString();
			String accesslist_protocol = accessList.leaf(nfv._acl_protocol).valueAsString();
			String source_any = accessList.leaf(nfv._source_any).valueAsString();

			if (source_any != null && source_any.equals(Commons.truee))
				source_any = Commons.any;
			else
				source_any = "";

			String destination_any = accessList.leaf(nfv._destination_any).valueAsString();
			if (destination_any != null && destination_any.equals(Commons.truee))
				destination_any = Commons.any;
			else
				destination_any = "";

			String source_ip = accessList.leaf(nfv._source_ip).valueAsString();
			String source_wildcard = "";

			if (source_ip != null) {
				source_wildcard = Utility.getNetMask(source_ip);
				source_wildcard = Utility.getWildCardMask(source_wildcard);
				source_ip = Utility.getIPAddress(source_ip);
			} else
				source_ip = "";

			String destination_ip = accessList.leaf(nfv._destination_ip).valueAsString();
			String destination_wildcard = "";

			if (destination_ip != null) {
				destination_wildcard = Utility.getNetMask(destination_ip);
				destination_wildcard = Utility.getWildCardMask(destination_wildcard);
				destination_ip = Utility.getIPAddress(destination_ip);
			} else
				destination_ip = "";

			String icmp_type = accessList.leaf(nfv._icmp_type).valueAsString();
			String icmp_code = accessList.leaf(nfv._icmp_code).valueAsString();
			String icmp_message = accessList.leaf(nfv._icmp_message).valueAsString();
			String source_port = accessList.leaf(nfv._source_port).valueAsString();
			if (source_port != null && source_port.equalsIgnoreCase("none")) {
				source_port = null;
			}
			String destination_port = accessList.leaf(nfv._destination_port).valueAsString();
			if (destination_port != null && destination_port.equalsIgnoreCase("none")) {
				destination_port = null;
			}
			String source_operator = accessList.leaf(nfv._source_operator).valueAsString();
			if (source_operator != null && source_operator.equalsIgnoreCase("none")) {
				source_operator = null;
			}

			String destination_operator = accessList.leaf(nfv._destination_operator).valueAsString();

			if (destination_operator != null && destination_operator.equalsIgnoreCase("none")) {
				destination_operator = null;
			}

			String stAClRule = "";
			stAClRule = acl_seq_no + " " + acl_action + " " + accesslist_protocol;
			if (source_any != null && !source_any.equals(""))
				stAClRule = stAClRule + " " + source_any;
			if (source_ip != null && !source_ip.equals(""))
				stAClRule = stAClRule + " " + source_ip;
			if (source_wildcard != null && !source_wildcard.equals(""))
				stAClRule = stAClRule + " " + source_wildcard;
			if (source_operator != null)
				stAClRule = stAClRule + " " + source_operator;
			if (source_port != null)
				stAClRule = stAClRule + " " + source_port;
			if (destination_any != null && !destination_any.equals(""))
				stAClRule = stAClRule + " " + destination_any;
			if (destination_ip != null && !destination_ip.equals(""))
				stAClRule = stAClRule + " " + destination_ip;
			if (destination_wildcard != null && !destination_wildcard.equals(""))
				stAClRule = stAClRule + " " + destination_wildcard;
			if (destination_operator != null)
				stAClRule = stAClRule + " " + destination_operator;
			if (destination_port != null)
				stAClRule = stAClRule + " " + destination_port;
			if (icmp_type != null)
				stAClRule = stAClRule + " " + icmp_type;
			if (icmp_code != null)
				stAClRule = stAClRule + " " + icmp_code;
			if (icmp_message != null)
				stAClRule = stAClRule + " " + icmp_message;
			LOGGER.info("ACL rule:" + stAClRule);

			TemplateVariables vars = new TemplateVariables();
			vars.putQuoted(Commons.device, csrName);
			vars.putQuoted(Commons.interfacee, acl_interface_id);
			vars.putQuoted(Commons.accessListName, access_list_name);
			vars.putQuoted(Commons.rule, stAClRule);
			vars.putQuoted(Commons.accessListDirection, access_list_direction);
			aclListTemplate.apply(service, vars);
		}
		LOGGER.info("end ACL configuration");
	}
	// ******************* ACL configuration END *******************

}
