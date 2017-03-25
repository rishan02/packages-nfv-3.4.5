package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects;


/**
 * Description:<br>
 * Devices and other services this service has explicitly<br>
 * modified.<br>
 * <br>
 * tailf-info:<br>
 * Devices and other services this service has explicitly modified.<br>
 */
public class DirectlyModified extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "directly-modified";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "directly-modified";
    public static final java.lang.String CALL_POINT_NAME = "ncs";
    public static final java.lang.String LEAF_DEVICES_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEVICES_TAG = "devices";
    public static final java.lang.String LEAF_SERVICES_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_SERVICES_TAG = "services";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> _devices;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> _services;

    public DirectlyModified(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        super(null);
        this.parent = parent;
    }

    public DirectlyModified(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * tailf-info:<br>
     * Devices this service has explicitly modified.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: devices</li>
     * <li> Yang type: leafref [/ncs:devices/ncs:device/ncs:name]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> devices() {
        if (this._devices == null) {
            this._devices = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>>(this, LEAF_DEVICES_PREFIX, LEAF_DEVICES_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.String>(com.cisco.singtel.resourcemanager.base.Converters.confBufConverter));
        }
        return this._devices;
    }

    /**
     * tailf-info:<br>
     * Services this service has explicitly modified.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: services</li>
     * <li> Yang type: instance-identifier</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> services() {
        if (this._services == null) {
            this._services = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>>(this, LEAF_SERVICES_PREFIX, LEAF_SERVICES_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.String>(new com.cisco.singtel.resourcemanager.base.Converters.ConfValueConverter("/singtel-rm:resource-manager/singtel-rm:pools/singtel-rm:object-pool/singtel-rm:objects/singtel-rm:directly-modified/singtel-rm:services")));
        }
        return this._services;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
            super._setPath(parent._getPath().go(TAG));
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
        this.devices()._unscheduledDelete();
        this.services()._unscheduledDelete();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects detached = this.parent;
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

    public DirectlyModified _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects)null);
        return this;
    }

    public DirectlyModified _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public DirectlyModified _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.devices()._configureReader(reader);
            this.services()._configureReader(reader);

        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.devices()._collectFromReader(reader);
            this.services()._collectFromReader(reader);

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

    public DirectlyModified _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("directly-modified").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("directly-modified");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        buffer.append(nextIndent).append("devices").append(":");
        if (this.devices().exists()) {
            for (java.lang.Object elem : this.devices().get()) {
                buffer.append(java.lang.System.lineSeparator()).append(nextIndent + indentStep).append(elem);
            }
        } else {
            buffer.append(" null");
        }

        buffer.append(java.lang.System.lineSeparator());
        buffer.append(nextIndent).append("services").append(":");
        if (this.services().exists()) {
            for (java.lang.Object elem : this.services().get()) {
                buffer.append(java.lang.System.lineSeparator()).append(nextIndent + indentStep).append(elem);
            }
        } else {
            buffer.append(" null");
        }

        buffer.append(java.lang.System.lineSeparator());
    }
}
