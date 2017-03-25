package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools;


public class HostIdPool extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "host-id-pool";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "host-id-pool";
    public static final java.lang.String LEAF_ALL_ALLOCATED_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_ALL_ALLOCATED_TAG = "all-allocated";
    public static final java.lang.String LEAF_LAST_ALLOCATED_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_LAST_ALLOCATED_TAG = "last-allocated";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.Integer>> _allAllocated;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Integer> _lastAllocated;

    public HostIdPool(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools parent) {
        super(null);
        this.parent = parent;
    }

    public HostIdPool(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: all-allocated</li>
     * <li> Yang type: uint16</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.Integer>> allAllocated() {
        if (this._allAllocated == null) {
            this._allAllocated = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.Integer>>(this, LEAF_ALL_ALLOCATED_PREFIX, LEAF_ALL_ALLOCATED_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.Integer>(com.cisco.singtel.resourcemanager.base.Converters.confUInt16Converter));
        }
        return this._allAllocated;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: last-allocated</li>
     * <li> Yang type: uint16</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Integer> lastAllocated() {
        if (this._lastAllocated == null) {
            this._lastAllocated = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Integer>(this, LEAF_LAST_ALLOCATED_PREFIX, LEAF_LAST_ALLOCATED_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt16Converter);
        }
        return this._lastAllocated;
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
        this.allAllocated()._unscheduledDelete();
        this.lastAllocated()._unscheduledDelete();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools detached = this.parent;
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

    public HostIdPool _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools)null);
        return this;
    }

    public HostIdPool _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public HostIdPool _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            this.allAllocated()._configureReader(reader);
            this.lastAllocated()._configureReader(reader);

        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            this.allAllocated()._collectFromReader(reader);
            this.lastAllocated()._collectFromReader(reader);

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

    public HostIdPool _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("host-id-pool").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("host-id-pool");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        buffer.append(nextIndent).append("allAllocated").append(":");
        if (this.allAllocated().exists()) {
            for (java.lang.Object elem : this.allAllocated().get()) {
                buffer.append(java.lang.System.lineSeparator()).append(nextIndent + indentStep).append(elem);
            }
        } else {
            buffer.append(" null");
        }

        buffer.append(java.lang.System.lineSeparator());
        if (this.lastAllocated().exists()) {
            buffer.append(nextIndent).append("lastAllocated").append(": ").append(this.lastAllocated().get()).append(java.lang.System.lineSeparator());
        }
    }
}
