package com.singtel.nfv.model.vcpe.createvcpe;


public class Output extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "output";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:output";
    public static final java.lang.String LEAF_RESULT_PREFIX = "vcpe";
    public static final java.lang.String LEAF_RESULT_TAG = "result";
    public static final java.lang.String LEAF_HOSTNAME_PREFIX = "vcpe";
    public static final java.lang.String LEAF_HOSTNAME_TAG = "hostname";
    public static final java.lang.String LEAF_INGRESS_ID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_INGRESS_ID_TAG = "ingress-id";
    public static final java.lang.String LEAF_EGRESS_ID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_EGRESS_ID_TAG = "egress-id";
    public static final java.lang.String LEAF_MANAGEMENT_IP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_MANAGEMENT_IP_TAG = "management-ip";
    public static final java.lang.String LEAF_ERRNO_PREFIX = "vcpe";
    public static final java.lang.String LEAF_ERRNO_TAG = "errno";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.output.Result> _result;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _hostname;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _ingressId;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _egressId;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _managementIp;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.output.Errno> _errno;

    public Output(com.singtel.nfv.model.vcpe.CreateVcpe parent) {
        super(null);
    }

    public Output(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: result</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.output.Result> result() {
        if (this._result == null) {
            this._result = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.output.Result>(this, LEAF_RESULT_PREFIX, LEAF_RESULT_TAG, com.singtel.nfv.model.vcpe.createvcpe.output.Result.CONVERTER);
        }
        return this._result;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: hostname</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> hostname() {
        if (this._hostname == null) {
            this._hostname = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_HOSTNAME_PREFIX, LEAF_HOSTNAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._hostname;
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
     * tailf-info:<br>
     * Management ip of csr1kv<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: management-ip</li>
     * <li> Yang type: inet:ipv4-address</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> managementIp() {
        if (this._managementIp == null) {
            this._managementIp = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_MANAGEMENT_IP_PREFIX, LEAF_MANAGEMENT_IP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4Converter);
        }
        return this._managementIp;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: errno</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.output.Errno> errno() {
        if (this._errno == null) {
            this._errno = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.createvcpe.output.Errno>(this, LEAF_ERRNO_PREFIX, LEAF_ERRNO_TAG, com.singtel.nfv.model.vcpe.createvcpe.output.Errno.CONVERTER);
        }
        return this._errno;
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
        this.result()._unscheduledDelete();
        this.hostname()._unscheduledDelete();
        this.ingressId()._unscheduledDelete();
        this.egressId()._unscheduledDelete();
        this.managementIp()._unscheduledDelete();
        this.errno()._unscheduledDelete();
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        return false;
    }

    public Output _fromConfXMLParams(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
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
                case "vcpe:result": {
                    this.result()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:hostname": {
                    this.hostname()._unscheduledSet(param.getValue());
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
                case "vcpe:management-ip": {
                    this.managementIp()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:errno": {
                    this.errno()._unscheduledSet(param.getValue());
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
        if (this.result()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.result()._getPrefix(), this.result()._getTag(), this.result().toConfValue()));
        }
        if (this.hostname()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.hostname()._getPrefix(), this.hostname()._getTag(), this.hostname().toConfValue()));
        }
        if (this.ingressId()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.ingressId()._getPrefix(), this.ingressId()._getTag(), this.ingressId().toConfValue()));
        }
        if (this.egressId()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.egressId()._getPrefix(), this.egressId()._getTag(), this.egressId().toConfValue()));
        }
        if (this.managementIp()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.managementIp()._getPrefix(), this.managementIp()._getTag(), this.managementIp().toConfValue()));
        }
        if (this.errno()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.errno()._getPrefix(), this.errno()._getTag(), this.errno().toConfValue()));
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.result().exists()) {
            buffer.append(nextIndent).append("result").append(": ").append(this.result().get()).append(java.lang.System.lineSeparator());
        }
        if (this.hostname().exists()) {
            buffer.append(nextIndent).append("hostname").append(": ").append(this.hostname().get()).append(java.lang.System.lineSeparator());
        }
        if (this.ingressId().exists()) {
            buffer.append(nextIndent).append("ingressId").append(": ").append(this.ingressId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.egressId().exists()) {
            buffer.append(nextIndent).append("egressId").append(": ").append(this.egressId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.managementIp().exists()) {
            buffer.append(nextIndent).append("managementIp").append(": ").append(this.managementIp().get()).append(java.lang.System.lineSeparator());
        }
        if (this.errno().exists()) {
            buffer.append(nextIndent).append("errno").append(": ").append(this.errno().get()).append(java.lang.System.lineSeparator());
        }
    }
}
