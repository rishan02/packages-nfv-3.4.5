package com.singtel.nfv;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.singtel.nfv.namespaces.nfv;
import com.tailf.conf.ConfException;
import com.tailf.dp.services.ServiceContext;
import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuNode;
import com.tailf.ncs.template.Template;
import com.tailf.ncs.template.TemplateVariables;

public class DayOneConf {
	private static Logger LOGGER = Logger.getLogger(DayTwoConf.class);
	ServiceContext context;
	NavuNode service;
	String csrName;
	String hostName;

	DayOneConf(ServiceContext context, NavuNode service, String csrName, String hostName) {
		this.context = context;
		this.service = service;
		this.csrName = csrName;
		this.hostName = hostName;
	}

	public void applyConfiguration() throws ConfException, UnknownHostException
	{
		Template csrDayOneTemplate = new Template(context, Commons.csrDayOneTemplate);
		Template subInterface = new Template(context, Commons.subinterfaceTemplate);
		TemplateVariables vars = new TemplateVariables();
		LOGGER.info("Start day-1 configuration");
		
		String lanIp = service.leaf(nfv._lan_ip).valueAsString();
		String wanIp = service.leaf(nfv._wan_ip).valueAsString();

		String lanValue = lanIp.substring(0, lanIp.indexOf('/'));
		String wanValue = wanIp.substring(0, wanIp.indexOf('/'));
		String lan_ip_network = Utility.getNetAddr(lanIp);
		vars.putQuoted(Commons.device, csrName);
		vars.putQuoted(Commons.mask2, Utility.getNetMask(wanIp));
		vars.putQuoted(Commons.address2, wanValue);
		vars.putQuoted(Commons.mask, Utility.getNetMask(lanIp));
		vars.putQuoted(Commons.address, lanValue);
		vars.putQuoted(Commons.lanIp, lan_ip_network);
		vars.putQuoted(Commons.hostname, hostName);
		LOGGER.info("WAN IP Mask value : " + Utility.getNetMask(wanIp));
		LOGGER.info("GigabitEthernet 2 template - day one configuration applied");
		LOGGER.info("LAN IP Mask value : " + Utility.getNetMask(lanIp));
		LOGGER.info("NETWORK ADDRESS of lan_ip" + lan_ip_network);
		csrDayOneTemplate.apply(service, vars);
		LOGGER.info("start subinterface configuration");
		for (NavuNode cVlan : service.list("lan-cvlans")) {
			String lancvanvalId = cVlan.leaf("lan-cvlan-id").valueAsString();
			String lanipAddress = cVlan.leaf("lan-cvlan-ip").valueAsString();
			if(lanipAddress !=null){
			vars.putQuoted("lansubinterface", "3." + lancvanvalId);
			vars.putQuoted("lancvanMask", Utility.getNetMask(lanipAddress));
			vars.putQuoted("lancvanIp", lanipAddress.substring(0, lanipAddress.indexOf('/')));
			vars.putQuoted("wansubinterface", "");
			vars.putQuoted("wancvanMask", "");
			vars.putQuoted("wancvanIp", "");
			subInterface.apply(service, vars);
			}
			else{
			LOGGER.info("lan-ip address and length are not provided");
			}	
		}
		for (NavuNode cVlan : service.list("wan-cvlans")) {
			String wancvanvalId = cVlan.leaf("wan-cvlan-id").valueAsString();
			String wanipAddress = cVlan.leaf("wan-cvlan-ip").valueAsString();
			if(wanipAddress !=null){
			vars.putQuoted("wansubinterface", "2." + wancvanvalId);
			vars.putQuoted("wancvanMask", Utility.getNetMask(wanipAddress));
			vars.putQuoted("wancvanIp", wanipAddress.substring(0, wanipAddress.indexOf('/')));
			vars.putQuoted("lansubinterface", "");
			vars.putQuoted("lancvanMask", "");
			vars.putQuoted("lancvanIp", "");
			subInterface.apply(service, vars);
			}
			else
			{
				LOGGER.info("wan-ip address and length are not provided");
			}
		}
		LOGGER.info("end subinterface configurations");
		LOGGER.info("End day-1 configuration");
	}

	
}
