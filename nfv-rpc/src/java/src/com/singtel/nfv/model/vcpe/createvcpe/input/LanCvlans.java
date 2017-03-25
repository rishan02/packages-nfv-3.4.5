package com.singtel.nfv.model.vcpe.createvcpe.input;


/**
 * tailf-info:<br>
 * Ingress VLAN subinterfaces<br>
 */
public class LanCvlans extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "lan-cvlans";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "lan-cvlans";
    public static final java.lang.String LEAF_LAN_CVLAN_ID_PREFIX = "vcpe";
    public static final java.lang.String LEAF_LAN_CVLAN_ID_TAG = "lan-cvlan-id";
    public static final java.lang.String LEAF_LAN_CVLAN_IP_PREFIX = "vcpe";
    public static final java.lang.String LEAF_LAN_CVLAN_IP_TAG = "lan-cvlan-ip";
    private com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList parent;
    private boolean exists = false;
    private LanCvlansListKey key;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _lanCvlanId;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _lanCvlanIp;

    public LanCvlans(com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList parent, LanCvlansListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public LanCvlans(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new LanCvlansListKey(path.getKey()));
    }

    protected LanCvlans() {
        super(null);
        this.parent = null;
        this.key = null;
    }

    /**
     * tailf-info:<br>
     * LAN subinterface id<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: lan-cvlan-id</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> lanCvlanId() {
        if (this._lanCvlanId == null) {
            this._lanCvlanId = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_LAN_CVLAN_ID_PREFIX, LEAF_LAN_CVLAN_ID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._lanCvlanId;
    }

    /**
     * tailf-info:<br>
     * LAN subinterface IP Address<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: lan-cvlan-ip</li>
     * <li> Yang type: tailf:ipv4-address-and-prefix-length</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> lanCvlanIp() {
        if (this._lanCvlanIp == null) {
            this._lanCvlanIp = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_LAN_CVLAN_IP_PREFIX, LEAF_LAN_CVLAN_IP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4AndPrefixLengthConverter);
        }
        return this._lanCvlanIp;
    }

    public LanCvlansListKey _getKey() {
        return this.key;
    }

    protected void _setKey(LanCvlansListKey key) {
        this.lanCvlanId()._unscheduledSet(key.lanCvlanId());

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
        this.lanCvlanId()._unscheduledDelete();
        this.lanCvlanIp()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList _parent() {
        if (this.parent == null) {
            this.parent = new com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList _attach(com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList detached = this.parent;
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

    public LanCvlans _detach() {
        this._attach((com.singtel.nfv.model.vcpe.createvcpe.input.LanCvlansList)null);
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
                case "vcpe:lan-cvlan-id": {
                    this.lanCvlanId()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:lan-cvlan-ip": {
                    this.lanCvlanIp()._unscheduledSet(param.getValue());
                    break;
                }
                default: {
                    throw new com.tailf.conf.ConfException("Unknown element: " + param.getTag());
                }
            }
        }


        this.key = new LanCvlansListKey(this.lanCvlanId().get());

        this.exists = true;

        return i;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        params.add(new com.tailf.conf.ConfXMLParamStart(this._getPrefix(), this._getTag()));
        if (this.lanCvlanId()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.lanCvlanId()._getPrefix(), this.lanCvlanId()._getTag(), this.lanCvlanId().toConfValue()));
        }
        if (this.lanCvlanIp()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.lanCvlanIp()._getPrefix(), this.lanCvlanIp()._getTag(), this.lanCvlanIp().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.lanCvlanId().exists()) {
            buffer.append(nextIndent).append("lanCvlanId").append(": ").append(this.lanCvlanId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.lanCvlanIp().exists()) {
            buffer.append(nextIndent).append("lanCvlanIp").append(": ").append(this.lanCvlanIp().get()).append(java.lang.System.lineSeparator());
        }
    }
}
