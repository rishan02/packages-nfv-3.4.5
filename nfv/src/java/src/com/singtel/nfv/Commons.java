package com.singtel.nfv;

public interface Commons {
	
	// Common Strings
	public static final String servicePoint = "l2nid-mpls-csr-servicepoint";
	public static final String deploymentName = "L2M";
	public static final String csr = "CSR";
	public static final String megapop = "megapop";
	public static final String serviceType = "service-type";
	public static final String ifName = "if-name";
	public static final String ifType = "if-type";
	public static final String ready = "ready";
	public static final String gigabitEthernet = "GigabitEthernet";
	public static final String lan = "3";
	public static final String wan = "2";
	public static final String zero = "0";
	public static final String two = "2";
	public static final String three = "3";
	public static final String wanS = "wan";	
	public static final String name2 = "name2";
	public static final String desc2 = "desc2";
	public static final String mask2 = "mask2";
	public static final String address2 = "address2";
	public static final String desc = "desc";
	public static final String mask = "mask";
	public static final String lanIp = "lan-ip";
	public static final String hostname = "hostname";	
	public static final String pool = "Pool";
	public static final String yes = "Yes";
	public static final String no ="No";
	public static final String truee = "true";
	public static final String falsee = "false";
	public static final String ipdhcp = "IPDHCP";
	public static final String cNo = "NO";
	public static final String maskroute = "maskroute";
	public static final String prefixroute = "prefixroute";
	public static final String nicidgateway = "nicidgateway";
	public static final String nicIdGateway = "nicidgateway";
	public static final String in = "in";
	public static final String out = "out";
	public static final String oneZeroTwo = "102";
	public static final String oneZeroThree = "103";
	public static final String oneZeroZero = "100";
	public static final String oneZeroOne = "101";
	public static final String any = "any";
	public static final String inProgress = "in-progress";
	public static final String operational = "operational";
	public static final String nonOperational = "non-operational";
	public static final String admin = "admin";
	public static final String userName = "USERNAME";
	public static final String password = "ADMIN_PWD";
	public static final String system = "system";
	public static final String nameC = "NAME";
	public static final String tenant = "TENANT";
	public static final String depName = "DEPNAME";
	public static final String serviceName = "SERVICE_NAME";
	public static final String serviceVersion = "SERVICE_VERSION";
	public static final String vmGroup = "VM_GROUP";
	public static final String ingressNet = "INGRESS_NET";
	public static final String egressNet = "EGRESS_NET";
	public static final String lanIpAddress ="LAN_IP_ADDRESS";
	public static final String wanIpAddress ="WAN_IP_ADDRESS";
	public static final String mgmtIp = "MGMT_IP";
	public static final String mgmtNetmask = "MGMT_NETMASK";
	public static final String ingressNetIp = "INGRESS_NET_IP";
	public static final String ingressNetmask = "INGRESS_NETMASK";
	public static final String egressNetIp = "EGRESS_NET_IP";
	public static final String lanMacAddress="LAN_MAC_ADDRESS";
	public static final String wanMacAddress="WAN_MAC_ADDRESS";
	public static final String egressNetmask = "EGRESS_NETMASK";
    public static final String ingressNetwork= "INGRESS_NETWORK";
    public static final String egressNetwork= "EGRESS_NETWORK";
    public static final String ingressVlanId ="INGRESS_VLANID";
    public static final String egressVlanId ="EGRESS_VLANID";
	public static final String authGroup="AUTH_GROUP";
	public static final String mgmtNetworkName="MGMT_NETWORK_NAME";
	public static final String mgmtNet = "MGMT_NET";
	public static final String network = "NETWORK";
	public static final String subNetAdd = "SUBNETADD";
	public static final String subNetMask = "SUBNETMASK";
	public static final String vLanId = "VLANID";
	public static final String dynamicTenant = "DYNAMIC_TENANT";
	public static final String vnfPhysicalNetwork = "VNF_PHYSICAL_NETWORK";
	public static final String remoteDevice = "remote-device";
	public static final String remoteIfType = "remote-if-type";
	public static final String remoteIfName = "remote-if-name";
	public static final String ipAddress = "ip-address";
	public static final String csrPort = "csr-port";
	public static final String ifNameC = "IFNAME";
	public static final String description = "DESCRIPTION";
	public static final String xConnect = "XCONNECT";
	public static final String _xConnect = "XCONNECT_";
	public static final String _lConnect = "LCONNECT_";
	public static final String p2p = "P2P";
	public static final String id1 = "ID1";
	public static final String id2 = "ID2";
	public static final String id1Vlanid1 = "id1_vlanid1";
	public static final String id2Vlanid1 = "id2_vlanid1";
	public static final String customer1_1 = "Customer1_1";
	public static final String customer1_2 = "Customer1_2";
	public static final String vlanid1_1 = "vlanid1_1";
	public static final String vlanid1_2 = "vlanid1_2";
	public static final String ifName1 = "IFNAME1";
	public static final String ifName2 = "IFNAME2";
	public static final String format = "%d.%d.%d.%d";
	public static final String rule = "rule";
	public static final String interfacee = "interface";
	public static final String accessListDirection = "access-list-direction";
	public static final String accessListName = "access-list-name";
	
	// Exception Messages
	public static final String cannotDeleteService = "Cannot delete service ";
	public static final String notAbleToCheckDevices = "Not able to check devices";
	
	// Navu Strings
	public static final String devices = "devices";
	public static final String device = "device";
	public static final String deviceC = "DEVICE";
	public static final String name = "name";
	public static final String esc = "ESC";
	public static final String dci = "dci";
	public static final String dcn = "dcn";
	public static final String address = "address";
	public static final String capability = "capability";
	public static final String mess = "Device %1$s has no known capabilities, " + "has sync-from been performed?";
	public static final String vlan = "vlan";
	public static final String osNet = "os-net";
	public static final String dciPort = "dci-port";
	
	// HTTP Client Strings
	public static final String http = "http://";
	public static final String escRestURL = ":8080/ESCManager/v0/deployments/";
	public static final String callBack = "Callback";
	public static final String callBackMethod = "/undeployservicecallback";
	
	// Template Strings
	public static final String dciTemplate = "dci-template";
	public static final String createCsrVmTemplate = "create-csr-nfvo-vnf-info-template";
	public static final String csrDayOneTemplate = "csr-day-one-template";
	public static final String subinterfaceTemplate = "subinterface";
	public static final String bgpPrefixListTemplate = "bgp-prefix-list-template";
	public static final String staticRouteListTemplate = "static-route-list-template";
	public static final String natStaticListTemplate = "nat-static-list-template";
	public static final String patStaticListTemplate = "pat-static-list-template";
	public static final String aclListTemplate = "acl-list-template";
	public static final String extAclListTemplate = "ext-acl-list-template";
	public static final String serviceRedeployKicker = "service-nfvo-vnf-info-kicker";
	public static final String dhcpTemplate="dhcp-template";
	public static final String macAddressTemplate="mac-address-template";
	

	
	//Drop3 Template
	public static final String drop3CommonTemmplate="drop3common-template";
	public static final String eigrpTemplate="eigrp-template";
	public static final String ospfTemplate="ospf-template";
	public static final String hsrpTemplate="hsrp-template";
	public static final String vrrpTemplate="vrrp-template";
	public static final String prefixListTemplate = "prefix-list-template";
	public static final String bgpTemplate="bgp-template";
	public static final String qosTemplate = "qos-template";
	public static final String trackObjectTemplate = "track-object-template";
	
	// Routing Protocol / Network Strings
	public static final String ingress = "-ingress";
	public static final String egress = "-egress";
	public static final String defaultEscServiceName = "nfv-csr-service";
	public static final String defaultEscServiceVersion = "1.0";
	public static final String defaultEscVMGroup = "CSR";
	
	// ACL strings
	public static final String lan1="lan1";
	public static final String lan1In="acl-lan1-in";
	public static final String lan1Out="acl-lan1-out";
	public static final String wanIn="acl-wan-in";
	public static final String wanOut="acl-wan-out";
	
	public static final String sitesUri="http://com/singtel/sites";
	
	// Plan component states
	public static final String SERVICE_CREATION_SUCCESS="reached";
	public static final String SERVICE_CREATION_FAILED="failed";
	public static final String SERVICE_CREATION_NOT_REACHED="not-reached";
	
	// Nfvo Mano
	public static final String ADMIN_TENANT = "admin";
	public static final String VNFD_NAME="CSR1kv";
	public static final String VDU_NAME="CSR";
	public static final String SERVICE_READY_STATE_PATH = "/nfvo-rel2:nfvo/vnf-info/nfvo-rel2-esc:esc/vnf-deployment{%s %s %s}/plan/component{self}/state{ncs:ready}";
}
