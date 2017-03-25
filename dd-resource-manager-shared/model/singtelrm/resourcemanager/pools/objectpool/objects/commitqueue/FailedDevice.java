package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue;


/**
 * Description:<br>
 * If the 'status' leaf is 'failed', this list contains detailed<br>
 * information on the devices that were responsible for the<br>
 * error of the commit queue item.<br>
 */
public class FailedDevice extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "failed-device";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "failed-device";
    public static final java.lang.String LEAF_NAME_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NAME_TAG = "name";
    public static final java.lang.String LEAF_TIME_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_TIME_TAG = "time";
    public static final java.lang.String LEAF_CONFIG_DATA_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_CONFIG_DATA_TAG = "config-data";
    public static final java.lang.String LEAF_ERROR_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_ERROR_TAG = "error";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList parent;
    private boolean exists = false;
    private FailedDeviceListKey key;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _name;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _time;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _configData;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _error;

    public FailedDevice(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList parent, FailedDeviceListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public FailedDevice(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new FailedDeviceListKey(path.getKey()));
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: name</li>
     * <li> Yang type: leafref [/ncs:devices/ncs:device/ncs:name]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> name() {
        if (this._name == null) {
            this._name = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NAME_PREFIX, LEAF_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._name;
    }

    /**
     * Description:<br>
     * Time when the failure occured.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: time</li>
     * <li> Yang type: yang:date-and-time</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> time() {
        if (this._time == null) {
            this._time = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_TIME_PREFIX, LEAF_TIME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._time;
    }

    /**
     * Description:<br>
     * The configuration data that was rejected by the device,<br>
     * i.e., the data that was sent from NCS to the device.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: config-data</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> configData() {
        if (this._configData == null) {
            this._configData = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_CONFIG_DATA_PREFIX, LEAF_CONFIG_DATA_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._configData;
    }

    /**
     * Description:<br>
     * The error returned by the device.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: error</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> error() {
        if (this._error == null) {
            this._error = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_ERROR_PREFIX, LEAF_ERROR_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._error;
    }

    public FailedDeviceListKey _getKey() {
        return this.key;
    }

    protected void _setKey(FailedDeviceListKey key) {
        this.name()._unscheduledSet(key.name());

        this.key = key;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
            super._setPath(parent._getPath().elem(this._getKey().asConfKey()));
        }

        return super._getPath();
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
        this.time()._unscheduledDelete();
        this.configData()._unscheduledDelete();
        this.error()._unscheduledDelete();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList detached = this.parent;
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

    public FailedDevice _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList)null);
        return this;
    }

    public FailedDevice _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public FailedDevice _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.time()._configureReader(reader);
            this.configData()._configureReader(reader);
            this.error()._configureReader(reader);

        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.time()._collectFromReader(reader);
            this.configData()._collectFromReader(reader);
            this.error()._collectFromReader(reader);

        }
    }

    @Override
    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.DataNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        if (super._scheduleWrite(child) && parent != null) {
            parent._scheduleWrite(this);
            return true;
        }

        return false;
    }

    public FailedDevice _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.elem(this._getKey().asConfKey()).setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.elem(this._getKey().asConfKey());
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.name().exists()) {
            buffer.append(nextIndent).append("name").append(": ").append(this.name().get()).append(java.lang.System.lineSeparator());
        }
        if (this.time().exists()) {
            buffer.append(nextIndent).append("time").append(": ").append(this.time().get()).append(java.lang.System.lineSeparator());
        }
        if (this.configData().exists()) {
            buffer.append(nextIndent).append("configData").append(": ").append(this.configData().get()).append(java.lang.System.lineSeparator());
        }
        if (this.error().exists()) {
            buffer.append(nextIndent).append("error").append(": ").append(this.error().get()).append(java.lang.System.lineSeparator());
        }
    }
}
