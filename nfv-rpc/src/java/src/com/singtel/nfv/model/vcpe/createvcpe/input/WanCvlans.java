package com.singtel.nfv.model.vcpe.createvcpe.input;


/**
 * tailf-info:<br>
 * Egress VLAN subinterfaces<br>
 */
public class WanCvlans extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "wan-cvlans";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "wan-cvlans";
    public static final java.lang.String LEAF_WAN_CVLAN_ID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_WAN_CVLAN_ID_TAG = "wan-cvlan-id";
    public static final java.lang.String LEAF_WAN_CVLAN_IP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_WAN_CVLAN_IP_TAG = "wan-cvlan-ip";
    private com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList parent;
    private boolean exists = false;
    private WanCvlansListKey key;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _wanCvlanId;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _wanCvlanIp;

    public WanCvlans(com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList parent, WanCvlansListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public WanCvlans(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new WanCvlansListKey(path.getKey()));
    }

    protected WanCvlans() {
        super(null);
        this.parent = null;
        this.key = null;
    }

    /**
     * tailf-info:<br>
     * WAN subinterface id<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: wan-cvlan-id</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> wanCvlanId() {
        if (this._wanCvlanId == null) {
            this._wanCvlanId = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_WAN_CVLAN_ID_PREFIX, LEAF_WAN_CVLAN_ID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._wanCvlanId;
    }

    /**
     * tailf-info:<br>
     * WAN subinterface IP Address<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: wan-cvlan-ip</li>
     * <li> Yang type: tailf:ipv4-address-and-prefix-length</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> wanCvlanIp() {
        if (this._wanCvlanIp == null) {
            this._wanCvlanIp = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_WAN_CVLAN_IP_PREFIX, LEAF_WAN_CVLAN_IP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4AndPrefixLengthConverter);
        }
        return this._wanCvlanIp;
    }

    public WanCvlansListKey _getKey() {
        return this.key;
    }

    protected void _setKey(WanCvlansListKey key) {
        this.wanCvlanId()._unscheduledSet(key.wanCvlanId());

        this.key = key;
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
        this.parent._scheduleWrite(this);
    }

    public void _delete() {
        this.exists = false;
        this.wanCvlanId()._unscheduledDelete();
        this.wanCvlanIp()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList _parent() {
        if (this.parent == null) {
            this.parent = new com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList _attach(com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList detached = this.parent;
                this.parent = null;
                detached._detach(this);
            }

            this.parent = parent;

            if (parent != null) {
                parent._attach(this);
            }
        }

        return parent;
    }

    public WanCvlans _detach() {
        this._attach((com.singtel.nfv.model.vcpe.createvcpe.input.WanCvlansList)null);
        return this;
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
            if (parent != null && !parent._exists()) {
                parent._scheduleWrite(this);
            }
        }

        return false;
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
                case "vcpe:wan-cvlan-id": {
                    this.wanCvlanId()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:wan-cvlan-ip": {
                    this.wanCvlanIp()._unscheduledSet(param.getValue());
                    break;
                }
                default: {
                    throw new com.tailf.conf.ConfException("Unknown element: " + param.getTag());
                }
            }
        }


        this.key = new WanCvlansListKey(this.wanCvlanId().get());

        this.exists = true;

        return i;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        params.add(new com.tailf.conf.ConfXMLParamStart(this._getPrefix(), this._getTag()));
        if (this.wanCvlanId()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.wanCvlanId()._getPrefix(), this.wanCvlanId()._getTag(), this.wanCvlanId().toConfValue()));
        }
        if (this.wanCvlanIp()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.wanCvlanIp()._getPrefix(), this.wanCvlanIp()._getTag(), this.wanCvlanIp().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.wanCvlanId().exists()) {
            buffer.append(nextIndent).append("wanCvlanId").append(": ").append(this.wanCvlanId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.wanCvlanIp().exists()) {
            buffer.append(nextIndent).append("wanCvlanIp").append(": ").append(this.wanCvlanIp().get()).append(java.lang.System.lineSeparator());
        }
    }
}
