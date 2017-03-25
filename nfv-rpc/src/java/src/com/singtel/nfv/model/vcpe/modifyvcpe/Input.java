package com.singtel.nfv.model.vcpe.modifyvcpe;


public class Input extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "input";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:input";
    public static final java.lang.String LEAF_SERVICE_NAME_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SERVICE_NAME_TAG = "service-name";
    public static final java.lang.String LEAF_ACCESS_SERVICE_TYPE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_ACCESS_SERVICE_TYPE_TAG = "access-service-type";
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
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _serviceName;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.input.AccessServiceType> _accessServiceType;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _sid;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _lanIp;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _wanIp;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.input.RoutingProtocol> _routingProtocol;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _gateway;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _asNumber;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _remoteAsNumber;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _neighborPasswd;

    public Input(com.singtel.nfv.model.vcpe.ModifyVcpe parent) {
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
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.input.AccessServiceType> accessServiceType() {
        if (this._accessServiceType == null) {
            this._accessServiceType = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.input.AccessServiceType>(this, LEAF_ACCESS_SERVICE_TYPE_PREFIX, LEAF_ACCESS_SERVICE_TYPE_TAG, com.singtel.nfv.model.vcpe.modifyvcpe.input.AccessServiceType.CONVERTER);
        }
        return this._accessServiceType;
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
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.input.RoutingProtocol> routingProtocol() {
        if (this._routingProtocol == null) {
            this._routingProtocol = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.input.RoutingProtocol>(this, LEAF_ROUTING_PROTOCOL_PREFIX, LEAF_ROUTING_PROTOCOL_TAG, com.singtel.nfv.model.vcpe.modifyvcpe.input.RoutingProtocol.CONVERTER);
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
        this.sid()._unscheduledDelete();
        this.lanIp()._unscheduledDelete();
        this.wanIp()._unscheduledDelete();
        this.routingProtocol()._unscheduledDelete();
        this.gateway()._unscheduledDelete();
        this.asNumber()._unscheduledDelete();
        this.remoteAsNumber()._unscheduledDelete();
        this.neighborPasswd()._unscheduledDelete();
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
    }
}
