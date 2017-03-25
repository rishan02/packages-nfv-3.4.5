package com.singtel.nfv.model.vcpe.createvcpe;


public class Input extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "input";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:input";
    public static final java.lang.String LEAF_SERVICE_NAME_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SERVICE_NAME_TAG = "service-name";
    public static final java.lang.String LEAF_ACCESS_SERVICE_TYPE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_ACCESS_SERVICE_TYPE_TAG = "access-service-type";
    public static final java.lang.String LEAF_BRN_PREFIX = "vcpe";
    public static final java.lang.String LEAF_BRN_TAG = "brn";
    public static final java.lang.String LEAF_BAN_PREFIX = "vcpe";
    public static final java.lang.String LEAF_BAN_TAG = "ban";
    public static final java.lang.String LEAF_SID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SID_TAG = "sid";
    public static final java.lang.String LEAF_LAN_IP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_LAN_IP_TAG = "lan-ip";
    public static final java.lang.String LEAF_WAN_IP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_WAN_IP_TAG = "wan-ip";
    public static final java.lang.String LEAF_ROUTING_PROTOCOL_PREFIX = "vcpe";
    public static final java.lang.String LEAF_ROUTING_PROTOCOL_TAG = "routing-protocol";
    public static final java.lang.String LEAF_GATEWAY_PREFIX = "vcpe";
    public static final java.lang.String LEAF_GATEWAY_TAG = "gateway";
    public static final java.lang.String LEAF_AS_NUMBER_PREFIX = "vcpe";
    public static final java.lang.String LEAF_AS_NUMBER_TAG = "as-number";
    public static final java.lang.String LEAF_REMOTE_AS_NUMBER_PREFIX = "vcpe";
    public static final java.lang.String LEAF_REMOTE_AS_NUMBER_TAG = "remote-as-number";
    public static final java.lang.String LEAF_NEIGHBOR_PASSWD_PREFIX = "vcpe";
    public static final java.lang.String LEAF_NEIGHBOR_PASSWD_TAG = "neighbor-passwd";
    public static final java.lang.String LEAF_SITE_CODE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SITE_CODE_TAG = "site-code";
    public static final java.lang.String LEAF_POD_PREFIX = "vcpe";
    public static final java.lang.String LEAF_POD_TAG = "pod";
    public static final java.lang.String LEAF_DCI_PREFIX = "vcpe";
    public static final java.lang.String LEAF_DCI_TAG = "dci";
    public static final java.lang.String LEAF_DCI_LINK_END_POINT_PREFIX = "vcpe";
    public static final java.lang.String LEAF_DCI_LINK_END_POINT_TAG = "dci-link-end-point";
    public static final java.lang.String LEAF_INGRESS_ID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_INGRESS_ID_TAG = "ingress-id";
    public static final java.lang.String LEAF_EGRESS_ID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_EGRESS_ID_TAG = "egress-id";
    public static final java.lang.String LEAF_INGRESS_MAC_ADDRESS_PREFIX = "vcpe";
    public static final java.lang.String LEAF_INGRESS_MAC_ADDRESS_TAG = "ingress-mac-address";
    public static final java.lang.String LEAF_EGRESS_MAC_ADDRESS_PREFIX = "vcpe";
    public static final java.lang.String LEAF_EGRESS_MAC_ADDRESS_TAG = "egress-mac-address";
    public static final java.lang.String LEAF_NFV_VAS_REFERENCE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_NFV_VAS_REFERENCE_TAG = "nfv-vas-reference";
    public static final java.lang.String LEAF_PRODUCT_CODE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_PRODUCT_CODE_TAG = "product-code";
    public static final java.lang.String LEAF_NFV_VAS_SCHEME_PREFIX = "vcpe";
    public static final java.lang.String LEAF_NFV_VAS_SCHEME_TAG = "nfv-vas-scheme";
    public static final java.lang.String LEAF_BRAND_PREFIX = "vcpe";
    public static final java.lang.String LEAF_BRAND_TAG = "brand";
    public static final java.lang.String LEAF_NFV_SETUP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_NFV_SETUP_TAG = "nfv-setup";
    public static final java.lang.String LEAF_NFV_ACCESS_SETUP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_NFV_ACCESS_SETUP_TAG = "nfv-access-setup";
    public static final java.lang.String LEAF_CIRCUIT_REFERENCE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_CIRCUIT_REFERENCE_TAG = "circuit-reference";
    public static final java.lang.String LEAF_SPEED_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SPEED_TAG = "speed";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _serviceName;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.AccessServiceType> _accessServiceType;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _brn;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _ban;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _sid;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _lanIp;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _wanIp;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.RoutingProtocol> _routingProtocol;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _gateway;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _asNumber;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _remoteAsNumber;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _neighborPasswd;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _siteCode;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _pod;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _dci;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _dciLinkEndPoint;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _ingressId;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _egressId;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _ingressMacAddress;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _egressMacAddress;
    private com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList _lanCvlans;
    private com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList _wanCvlans;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _nfvVasReference;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _productCode;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvVasScheme> _nfvVasScheme;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.Brand> _brand;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvSetup> _nfvSetup;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvAccessSetup> _nfvAccessSetup;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _circuitReference;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _speed;

    public Input(com.singtel.nfv.model.vcpe.CreateVcpe parent) {
        super(null);
    }

    public Input(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * tailf-info:<br>
     * Name of the vcpe service<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: service-name</li>
     * <li> Yang type: string</li>
     * <li> Ranges: 1..20</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> serviceName() {
        if (this._serviceName == null) {
            this._serviceName = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_SERVICE_NAME_PREFIX, LEAF_SERVICE_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._serviceName;
    }

    /**
     * tailf-info:<br>
     * Access service type<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: access-service-type</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.AccessServiceType> accessServiceType() {
        if (this._accessServiceType == null) {
            this._accessServiceType = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.AccessServiceType>(this, LEAF_ACCESS_SERVICE_TYPE_PREFIX, LEAF_ACCESS_SERVICE_TYPE_TAG, com.singtel.nfv.model.vcpe.createvcpe.input.AccessServiceType.CONVERTER);
        }
        return this._accessServiceType;
    }

    /**
     * tailf-info:<br>
     * Business Registration Number in Pegasus<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: brn</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> brn() {
        if (this._brn == null) {
            this._brn = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_BRN_PREFIX, LEAF_BRN_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._brn;
    }

    /**
     * tailf-info:<br>
     * Billing Account Number in Pegasus<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: ban</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> ban() {
        if (this._ban == null) {
            this._ban = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_BAN_PREFIX, LEAF_BAN_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._ban;
    }

    /**
     * tailf-info:<br>
     * Service Identifier<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: sid</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> sid() {
        if (this._sid == null) {
            this._sid = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_SID_PREFIX, LEAF_SID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._sid;
    }

    /**
     * tailf-info:<br>
     * LAN IP Address<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: lan-ip</li>
     * <li> Yang type: tailf:ipv4-address-and-prefix-length</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> lanIp() {
        if (this._lanIp == null) {
            this._lanIp = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_LAN_IP_PREFIX, LEAF_LAN_IP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4AndPrefixLengthConverter);
        }
        return this._lanIp;
    }

    /**
     * tailf-info:<br>
     * WAN IP Address<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: wan-ip</li>
     * <li> Yang type: tailf:ipv4-address-and-prefix-length</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> wanIp() {
        if (this._wanIp == null) {
            this._wanIp = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_WAN_IP_PREFIX, LEAF_WAN_IP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4AndPrefixLengthConverter);
        }
        return this._wanIp;
    }

    /**
     * tailf-info:<br>
     * Routing Protocol<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: routing-protocol</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.RoutingProtocol> routingProtocol() {
        if (this._routingProtocol == null) {
            this._routingProtocol = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.RoutingProtocol>(this, LEAF_ROUTING_PROTOCOL_PREFIX, LEAF_ROUTING_PROTOCOL_TAG, com.singtel.nfv.model.vcpe.createvcpe.input.RoutingProtocol.CONVERTER);
        }
        return this._routingProtocol;
    }

    /**
     * tailf-info:<br>
     * Gateway Address for Static Routing<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: gateway</li>
     * <li> Yang type: inet:ipv4-address</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> gateway() {
        if (this._gateway == null) {
            this._gateway = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_GATEWAY_PREFIX, LEAF_GATEWAY_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4Converter);
        }
        return this._gateway;
    }

    /**
     * tailf-info:<br>
     * BGP AS Number<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: as-number</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> asNumber() {
        if (this._asNumber == null) {
            this._asNumber = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_AS_NUMBER_PREFIX, LEAF_AS_NUMBER_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._asNumber;
    }

    /**
     * tailf-info:<br>
     * REMOTE AS Number<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: remote-as-number</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> remoteAsNumber() {
        if (this._remoteAsNumber == null) {
            this._remoteAsNumber = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_REMOTE_AS_NUMBER_PREFIX, LEAF_REMOTE_AS_NUMBER_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._remoteAsNumber;
    }

    /**
     * tailf-info:<br>
     * REMOTE AS Number<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: neighbor-passwd</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> neighborPasswd() {
        if (this._neighborPasswd == null) {
            this._neighborPasswd = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_NEIGHBOR_PASSWD_PREFIX, LEAF_NEIGHBOR_PASSWD_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._neighborPasswd;
    }

    /**
     * tailf-info:<br>
     * Site Code<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: site-code</li>
     * <li> Yang type: leafref [/sites:sites/sites:site/sites:site-code]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> siteCode() {
        if (this._siteCode == null) {
            this._siteCode = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_SITE_CODE_PREFIX, LEAF_SITE_CODE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._siteCode;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: pod</li>
     * <li> Yang type: leafref [/sites:sites/sites:site[sites:site-code=current()/../site-code]/sites:pods/sites:pod/sites:pod-name]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> pod() {
        if (this._pod == null) {
            this._pod = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_POD_PREFIX, LEAF_POD_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._pod;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: dci</li>
     * <li> Yang type: leafref [/sites:sites/sites:site[sites:site-code=current()/../site-code]/sites:pods/sites:pod[sites:pod-name=current()/../pod]/sites:dci/sites:dci-name]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> dci() {
        if (this._dci == null) {
            this._dci = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_DCI_PREFIX, LEAF_DCI_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._dci;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: dci-link-end-point</li>
     * <li> Yang type: leafref [/sites:sites/sites:site[sites:site-code=current()/../site-code]/sites:pods/sites:pod[sites:pod-name=current()/../pod]/sites:dci[sites:dci-name=current()/../dci]/sites:dci-link/sites:link-end-point-id]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> dciLinkEndPoint() {
        if (this._dciLinkEndPoint == null) {
            this._dciLinkEndPoint = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_DCI_LINK_END_POINT_PREFIX, LEAF_DCI_LINK_END_POINT_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._dciLinkEndPoint;
    }

    /**
     * tailf-info:<br>
     * Ingress VLAN Id<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: ingress-id</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> ingressId() {
        if (this._ingressId == null) {
            this._ingressId = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_INGRESS_ID_PREFIX, LEAF_INGRESS_ID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._ingressId;
    }

    /**
     * tailf-info:<br>
     * Egress VLAN Id<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: egress-id</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> egressId() {
        if (this._egressId == null) {
            this._egressId = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_EGRESS_ID_PREFIX, LEAF_EGRESS_ID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._egressId;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: ingress-mac-address</li>
     * <li> Yang type: nfvt:mac-address</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> ingressMacAddress() {
        if (this._ingressMacAddress == null) {
            this._ingressMacAddress = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_INGRESS_MAC_ADDRESS_PREFIX, LEAF_INGRESS_MAC_ADDRESS_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfValueConverter("/vcpe:create-vcpe/vcpe:input/vcpe:ingress-mac-address") /*Unhandled <nfvt:mac-address / LEAF>*/);
        }
        return this._ingressMacAddress;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: egress-mac-address</li>
     * <li> Yang type: nfvt:mac-address</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> egressMacAddress() {
        if (this._egressMacAddress == null) {
            this._egressMacAddress = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_EGRESS_MAC_ADDRESS_PREFIX, LEAF_EGRESS_MAC_ADDRESS_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfValueConverter("/vcpe:create-vcpe/vcpe:input/vcpe:egress-mac-address") /*Unhandled <nfvt:mac-address / LEAF>*/);
        }
        return this._egressMacAddress;
    }

    /**
     * tailf-info:<br>
     * Ingress VLAN subinterfaces<br>
     */
    public com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList lanCvlans() {
        if (this._lanCvlans == null) {
            this._lanCvlans = new com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList(this);
        }
        return this._lanCvlans;
    }

    /**
     * tailf-info:<br>
     * Egress VLAN subinterfaces<br>
     */
    public com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList wanCvlans() {
        if (this._wanCvlans == null) {
            this._wanCvlans = new com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList(this);
        }
        return this._wanCvlans;
    }

    /**
     * tailf-info:<br>
     * nfv vas reference<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-vas-reference</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> nfvVasReference() {
        if (this._nfvVasReference == null) {
            this._nfvVasReference = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_NFV_VAS_REFERENCE_PREFIX, LEAF_NFV_VAS_REFERENCE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._nfvVasReference;
    }

    /**
     * tailf-info:<br>
     * product code<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: product-code</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> productCode() {
        if (this._productCode == null) {
            this._productCode = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_PRODUCT_CODE_PREFIX, LEAF_PRODUCT_CODE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._productCode;
    }

    /**
     * tailf-info:<br>
     * nfv vas scheme<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-vas-scheme</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvVasScheme> nfvVasScheme() {
        if (this._nfvVasScheme == null) {
            this._nfvVasScheme = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvVasScheme>(this, LEAF_NFV_VAS_SCHEME_PREFIX, LEAF_NFV_VAS_SCHEME_TAG, com.singtel.nfv.model.vcpe.createvcpe.input.NfvVasScheme.CONVERTER);
        }
        return this._nfvVasScheme;
    }

    /**
     * tailf-info:<br>
     * Brand name<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: brand</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.Brand> brand() {
        if (this._brand == null) {
            this._brand = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.Brand>(this, LEAF_BRAND_PREFIX, LEAF_BRAND_TAG, com.singtel.nfv.model.vcpe.createvcpe.input.Brand.CONVERTER);
        }
        return this._brand;
    }

    /**
     * tailf-info:<br>
     * NFV setup name<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-setup</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvSetup> nfvSetup() {
        if (this._nfvSetup == null) {
            this._nfvSetup = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvSetup>(this, LEAF_NFV_SETUP_PREFIX, LEAF_NFV_SETUP_TAG, com.singtel.nfv.model.vcpe.createvcpe.input.NfvSetup.CONVERTER);
        }
        return this._nfvSetup;
    }

    /**
     * tailf-info:<br>
     * NFV access setup name<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-access-setup</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvAccessSetup> nfvAccessSetup() {
        if (this._nfvAccessSetup == null) {
            this._nfvAccessSetup = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.input.NfvAccessSetup>(this, LEAF_NFV_ACCESS_SETUP_PREFIX, LEAF_NFV_ACCESS_SETUP_TAG, com.singtel.nfv.model.vcpe.createvcpe.input.NfvAccessSetup.CONVERTER);
        }
        return this._nfvAccessSetup;
    }

    /**
     * tailf-info:<br>
     * circuit reference<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: circuit-reference</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> circuitReference() {
        if (this._circuitReference == null) {
            this._circuitReference = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_CIRCUIT_REFERENCE_PREFIX, LEAF_CIRCUIT_REFERENCE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._circuitReference;
    }

    /**
     * tailf-info:<br>
     * Speed value<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: speed</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> speed() {
        if (this._speed == null) {
            this._speed = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_SPEED_PREFIX, LEAF_SPEED_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._speed;
    }

    @Override
    public java.lang.String _getLabel() {
        return LABEL;
    }

    @Override
    public java.lang.String _getPrefix() {
        return PREFIX;
    }

    @Override
    public java.lang.String _getTag() {
        return TAG;
    }

    public boolean _exists() {
        return this.exists;
    }

    public void _create() {
        this.exists = true;
    }

    public void _delete() {
        this.exists = false;
        this.serviceName()._unscheduledDelete();
        this.accessServiceType()._unscheduledDelete();
        this.brn()._unscheduledDelete();
        this.ban()._unscheduledDelete();
        this.sid()._unscheduledDelete();
        this.lanIp()._unscheduledDelete();
        this.wanIp()._unscheduledDelete();
        this.routingProtocol()._unscheduledDelete();
        this.gateway()._unscheduledDelete();
        this.asNumber()._unscheduledDelete();
        this.remoteAsNumber()._unscheduledDelete();
        this.neighborPasswd()._unscheduledDelete();
        this.siteCode()._unscheduledDelete();
        this.pod()._unscheduledDelete();
        this.dci()._unscheduledDelete();
        this.dciLinkEndPoint()._unscheduledDelete();
        this.ingressId()._unscheduledDelete();
        this.egressId()._unscheduledDelete();
        this.ingressMacAddress()._unscheduledDelete();
        this.egressMacAddress()._unscheduledDelete();
        this.lanCvlans()._detach();
        this.wanCvlans()._detach();
        this.nfvVasReference()._unscheduledDelete();
        this.productCode()._unscheduledDelete();
        this.nfvVasScheme()._unscheduledDelete();
        this.brand()._unscheduledDelete();
        this.nfvSetup()._unscheduledDelete();
        this.nfvAccessSetup()._unscheduledDelete();
        this.circuitReference()._unscheduledDelete();
        this.speed()._unscheduledDelete();
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList _attach(com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList child) {
        if (this._lanCvlans != child) {
            if (this._lanCvlans != null) {
                com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList detached = this._lanCvlans;
                this._lanCvlans = null;
                detached._detach();
            }

            this._lanCvlans = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList _detach(com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList child) {
        com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList detached = this._lanCvlans;
        this._attach((com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList)null);
        return detached;
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList _attach(com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList child) {
        if (this._wanCvlans != child) {
            if (this._wanCvlans != null) {
                com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList detached = this._wanCvlans;
                this._wanCvlans = null;
                detached._detach();
            }

            this._wanCvlans = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList _detach(com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList child) {
        com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList detached = this._wanCvlans;
        this._attach((com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList)null);
        return detached;
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        return false;
    }

    public Input _fromConfXMLParams(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        this._parseConfXMLParams(params, 0, params.length);
        return this;
    }

    public com.tailf.conf.ConfXMLParam[] _toConfXMLParams() throws com.tailf.conf.ConfException {
        java.util.List<com.tailf.conf.ConfXMLParam> params = new java.util.ArrayList<>();
        this._writeConfXMLParams(params);
        return params.toArray(new com.tailf.conf.ConfXMLParam[params.size()]);
    }

    public int _parseConfXMLParams(com.tailf.conf.ConfXMLParam[] params, int start, int end) throws com.tailf.conf.ConfException {
        int i;
        for (i = start; i < end; i++) {
            com.tailf.conf.ConfXMLParam param = params[i];
            if (param instanceof com.tailf.conf.ConfXMLParamStop) {
                break;
            }

            java.lang.String prefix = null;
            com.tailf.conf.ConfNamespace ns = param.getConfNamespace();
            if (ns != null) {
                prefix = ns.prefix();
            }
            java.lang.String label = java.lang.String.format("%s:%s", prefix != null ? prefix : PREFIX, param.getTag());

            switch (label) {
                case "vcpe:service-name": {
                    this.serviceName()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:access-service-type": {
                    this.accessServiceType()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:brn": {
                    this.brn()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:ban": {
                    this.ban()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:sid": {
                    this.sid()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:lan-ip": {
                    this.lanIp()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:wan-ip": {
                    this.wanIp()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:routing-protocol": {
                    this.routingProtocol()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:gateway": {
                    this.gateway()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:as-number": {
                    this.asNumber()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:remote-as-number": {
                    this.remoteAsNumber()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:neighbor-passwd": {
                    this.neighborPasswd()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:site-code": {
                    this.siteCode()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:pod": {
                    this.pod()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:dci": {
                    this.dci()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:dci-link-end-point": {
                    this.dciLinkEndPoint()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:ingress-id": {
                    this.ingressId()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:egress-id": {
                    this.egressId()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:ingress-mac-address": {
                    this.ingressMacAddress()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:egress-mac-address": {
                    this.egressMacAddress()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:lan-cvlans": {
                    i = this.lanCvlans()._parseConfXMLParams(params, i + 1, end);
                    break;
                }
                case "vcpe:wan-cvlans": {
                    i = this.wanCvlans()._parseConfXMLParams(params, i + 1, end);
                    break;
                }
                case "vcpe:nfv-vas-reference": {
                    this.nfvVasReference()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:product-code": {
                    this.productCode()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:nfv-vas-scheme": {
                    this.nfvVasScheme()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:brand": {
                    this.brand()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:nfv-setup": {
                    this.nfvSetup()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:nfv-access-setup": {
                    this.nfvAccessSetup()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:circuit-reference": {
                    this.circuitReference()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:speed": {
                    this.speed()._unscheduledSet(param.getValue());
                    break;
                }
                default: {
                    throw new com.tailf.conf.ConfException("Unknown element: " + param.getTag());
                }
            }
        }


        this.exists = true;

        return i;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        if (this.serviceName()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.serviceName()._getPrefix(), this.serviceName()._getTag(), this.serviceName().toConfValue()));
        }
        if (this.accessServiceType()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.accessServiceType()._getPrefix(), this.accessServiceType()._getTag(), this.accessServiceType().toConfValue()));
        }
        if (this.brn()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.brn()._getPrefix(), this.brn()._getTag(), this.brn().toConfValue()));
        }
        if (this.ban()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.ban()._getPrefix(), this.ban()._getTag(), this.ban().toConfValue()));
        }
        if (this.sid()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.sid()._getPrefix(), this.sid()._getTag(), this.sid().toConfValue()));
        }
        if (this.lanIp()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.lanIp()._getPrefix(), this.lanIp()._getTag(), this.lanIp().toConfValue()));
        }
        if (this.wanIp()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.wanIp()._getPrefix(), this.wanIp()._getTag(), this.wanIp().toConfValue()));
        }
        if (this.routingProtocol()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.routingProtocol()._getPrefix(), this.routingProtocol()._getTag(), this.routingProtocol().toConfValue()));
        }
        if (this.gateway()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.gateway()._getPrefix(), this.gateway()._getTag(), this.gateway().toConfValue()));
        }
        if (this.asNumber()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.asNumber()._getPrefix(), this.asNumber()._getTag(), this.asNumber().toConfValue()));
        }
        if (this.remoteAsNumber()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.remoteAsNumber()._getPrefix(), this.remoteAsNumber()._getTag(), this.remoteAsNumber().toConfValue()));
        }
        if (this.neighborPasswd()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.neighborPasswd()._getPrefix(), this.neighborPasswd()._getTag(), this.neighborPasswd().toConfValue()));
        }
        if (this.siteCode()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.siteCode()._getPrefix(), this.siteCode()._getTag(), this.siteCode().toConfValue()));
        }
        if (this.pod()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.pod()._getPrefix(), this.pod()._getTag(), this.pod().toConfValue()));
        }
        if (this.dci()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.dci()._getPrefix(), this.dci()._getTag(), this.dci().toConfValue()));
        }
        if (this.dciLinkEndPoint()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.dciLinkEndPoint()._getPrefix(), this.dciLinkEndPoint()._getTag(), this.dciLinkEndPoint().toConfValue()));
        }
        if (this.ingressId()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.ingressId()._getPrefix(), this.ingressId()._getTag(), this.ingressId().toConfValue()));
        }
        if (this.egressId()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.egressId()._getPrefix(), this.egressId()._getTag(), this.egressId().toConfValue()));
        }
        if (this.ingressMacAddress()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.ingressMacAddress()._getPrefix(), this.ingressMacAddress()._getTag(), this.ingressMacAddress().toConfValue()));
        }
        if (this.egressMacAddress()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.egressMacAddress()._getPrefix(), this.egressMacAddress()._getTag(), this.egressMacAddress().toConfValue()));
        }
        if (this.lanCvlans()._exists()) {
            this.lanCvlans()._writeConfXMLParams(params);
        }
        if (this.wanCvlans()._exists()) {
            this.wanCvlans()._writeConfXMLParams(params);
        }
        if (this.nfvVasReference()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.nfvVasReference()._getPrefix(), this.nfvVasReference()._getTag(), this.nfvVasReference().toConfValue()));
        }
        if (this.productCode()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.productCode()._getPrefix(), this.productCode()._getTag(), this.productCode().toConfValue()));
        }
        if (this.nfvVasScheme()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.nfvVasScheme()._getPrefix(), this.nfvVasScheme()._getTag(), this.nfvVasScheme().toConfValue()));
        }
        if (this.brand()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.brand()._getPrefix(), this.brand()._getTag(), this.brand().toConfValue()));
        }
        if (this.nfvSetup()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.nfvSetup()._getPrefix(), this.nfvSetup()._getTag(), this.nfvSetup().toConfValue()));
        }
        if (this.nfvAccessSetup()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.nfvAccessSetup()._getPrefix(), this.nfvAccessSetup()._getTag(), this.nfvAccessSetup().toConfValue()));
        }
        if (this.circuitReference()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.circuitReference()._getPrefix(), this.circuitReference()._getTag(), this.circuitReference().toConfValue()));
        }
        if (this.speed()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.speed()._getPrefix(), this.speed()._getTag(), this.speed().toConfValue()));
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.serviceName().exists()) {
            buffer.append(nextIndent).append("serviceName").append(": ").append(this.serviceName().get()).append(java.lang.System.lineSeparator());
        }
        if (this.accessServiceType().exists()) {
            buffer.append(nextIndent).append("accessServiceType").append(": ").append(this.accessServiceType().get()).append(java.lang.System.lineSeparator());
        }
        if (this.brn().exists()) {
            buffer.append(nextIndent).append("brn").append(": ").append(this.brn().get()).append(java.lang.System.lineSeparator());
        }
        if (this.ban().exists()) {
            buffer.append(nextIndent).append("ban").append(": ").append(this.ban().get()).append(java.lang.System.lineSeparator());
        }
        if (this.sid().exists()) {
            buffer.append(nextIndent).append("sid").append(": ").append(this.sid().get()).append(java.lang.System.lineSeparator());
        }
        if (this.lanIp().exists()) {
            buffer.append(nextIndent).append("lanIp").append(": ").append(this.lanIp().get()).append(java.lang.System.lineSeparator());
        }
        if (this.wanIp().exists()) {
            buffer.append(nextIndent).append("wanIp").append(": ").append(this.wanIp().get()).append(java.lang.System.lineSeparator());
        }
        if (this.routingProtocol().exists()) {
            buffer.append(nextIndent).append("routingProtocol").append(": ").append(this.routingProtocol().get()).append(java.lang.System.lineSeparator());
        }
        if (this.gateway().exists()) {
            buffer.append(nextIndent).append("gateway").append(": ").append(this.gateway().get()).append(java.lang.System.lineSeparator());
        }
        if (this.asNumber().exists()) {
            buffer.append(nextIndent).append("asNumber").append(": ").append(this.asNumber().get()).append(java.lang.System.lineSeparator());
        }
        if (this.remoteAsNumber().exists()) {
            buffer.append(nextIndent).append("remoteAsNumber").append(": ").append(this.remoteAsNumber().get()).append(java.lang.System.lineSeparator());
        }
        if (this.neighborPasswd().exists()) {
            buffer.append(nextIndent).append("neighborPasswd").append(": ").append(this.neighborPasswd().get()).append(java.lang.System.lineSeparator());
        }
        if (this.siteCode().exists()) {
            buffer.append(nextIndent).append("siteCode").append(": ").append(this.siteCode().get()).append(java.lang.System.lineSeparator());
        }
        if (this.pod().exists()) {
            buffer.append(nextIndent).append("pod").append(": ").append(this.pod().get()).append(java.lang.System.lineSeparator());
        }
        if (this.dci().exists()) {
            buffer.append(nextIndent).append("dci").append(": ").append(this.dci().get()).append(java.lang.System.lineSeparator());
        }
        if (this.dciLinkEndPoint().exists()) {
            buffer.append(nextIndent).append("dciLinkEndPoint").append(": ").append(this.dciLinkEndPoint().get()).append(java.lang.System.lineSeparator());
        }
        if (this.ingressId().exists()) {
            buffer.append(nextIndent).append("ingressId").append(": ").append(this.ingressId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.egressId().exists()) {
            buffer.append(nextIndent).append("egressId").append(": ").append(this.egressId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.ingressMacAddress().exists()) {
            buffer.append(nextIndent).append("ingressMacAddress").append(": ").append(this.ingressMacAddress().get()).append(java.lang.System.lineSeparator());
        }
        if (this.egressMacAddress().exists()) {
            buffer.append(nextIndent).append("egressMacAddress").append(": ").append(this.egressMacAddress().get()).append(java.lang.System.lineSeparator());
        }
        this.lanCvlans()._formatTreeTo(buffer, nextIndent, indentStep);
        this.wanCvlans()._formatTreeTo(buffer, nextIndent, indentStep);
        if (this.nfvVasReference().exists()) {
            buffer.append(nextIndent).append("nfvVasReference").append(": ").append(this.nfvVasReference().get()).append(java.lang.System.lineSeparator());
        }
        if (this.productCode().exists()) {
            buffer.append(nextIndent).append("productCode").append(": ").append(this.productCode().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvVasScheme().exists()) {
            buffer.append(nextIndent).append("nfvVasScheme").append(": ").append(this.nfvVasScheme().get()).append(java.lang.System.lineSeparator());
        }
        if (this.brand().exists()) {
            buffer.append(nextIndent).append("brand").append(": ").append(this.brand().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvSetup().exists()) {
            buffer.append(nextIndent).append("nfvSetup").append(": ").append(this.nfvSetup().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvAccessSetup().exists()) {
            buffer.append(nextIndent).append("nfvAccessSetup").append(": ").append(this.nfvAccessSetup().get()).append(java.lang.System.lineSeparator());
        }
        if (this.circuitReference().exists()) {
            buffer.append(nextIndent).append("circuitReference").append(": ").append(this.circuitReference().get()).append(java.lang.System.lineSeparator());
        }
        if (this.speed().exists()) {
            buffer.append(nextIndent).append("speed").append(": ").append(this.speed().get()).append(java.lang.System.lineSeparator());
        }
    }
}
