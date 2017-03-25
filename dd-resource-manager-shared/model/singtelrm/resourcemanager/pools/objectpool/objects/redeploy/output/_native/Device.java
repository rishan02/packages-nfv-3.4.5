package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native;


public class Device extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "device";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "device";
    public static final java.lang.String LEAF_NAME_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NAME_TAG = "name";
    public static final java.lang.String LEAF_DATA_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DATA_TAG = "data";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList parent;
    private boolean exists = false;
    private DeviceListKey key;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _name;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _data;

    public Device(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList parent, DeviceListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public Device(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new DeviceListKey(path.getKey()));
    }

    protected Device() {
        super(null);
        this.parent = null;
        this.key = null;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: name</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> name() {
        if (this._name == null) {
            this._name = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_NAME_PREFIX, LEAF_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._name;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: data</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> data() {
        if (this._data == null) {
            this._data = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_DATA_PREFIX, LEAF_DATA_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._data;
    }

    public DeviceListKey _getKey() {
        return this.key;
    }

    protected void _setKey(DeviceListKey key) {

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
        this.name()._unscheduledDelete();
        this.data()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList detached = this.parent;
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

    public Device _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output._native.DeviceList)null);
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
                case "singtel-rm:name": {
                    this.name()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:data": {
                    this.data()._unscheduledSet(param.getValue());
                    break;
                }
                default: {
                    throw new com.tailf.conf.ConfException("Unknown element: " + param.getTag());
                }
            }
        }


        this.key = new DeviceListKey();

        this.exists = true;

        return i;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        params.add(new com.tailf.conf.ConfXMLParamStart(this._getPrefix(), this._getTag()));
        if (this.name()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.name()._getPrefix(), this.name()._getTag(), this.name().toConfValue()));
        }
        if (this.data()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.data()._getPrefix(), this.data()._getTag(), this.data().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.name().exists()) {
            buffer.append(nextIndent).append("name").append(": ").append(this.name().get()).append(java.lang.System.lineSeparator());
        }
        if (this.data().exists()) {
            buffer.append(nextIndent).append("data").append(": ").append(this.data().get()).append(java.lang.System.lineSeparator());
        }
    }
}
