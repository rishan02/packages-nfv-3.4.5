package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools;


public class IpPool extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "ip-pool";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "ip-pool";
    public static final java.lang.String LEAF_NAME_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NAME_TAG = "name";
    public static final java.lang.String LEAF_ALL_ALLOCATED_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_ALL_ALLOCATED_TAG = "all-allocated";
    public static final java.lang.String LEAF_LAST_ALLOCATED_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_LAST_ALLOCATED_TAG = "last-allocated";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList parent;
    private boolean exists = false;
    private IpPoolListKey key;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _name;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList _ranges;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> _allAllocated;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _lastAllocated;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList _allocatedByConsumer;

    public IpPool(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList parent, IpPoolListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public IpPool(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new IpPoolListKey(path.getKey()));
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: name</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> name() {
        if (this._name == null) {
            this._name = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NAME_PREFIX, LEAF_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._name;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList ranges() {
        if (this._ranges == null) {
            this._ranges = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList(this);
        }
        return this._ranges;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: all-allocated</li>
     * <li> Yang type: inet:ipv4-address</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> allAllocated() {
        if (this._allAllocated == null) {
            this._allAllocated = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>>(this, LEAF_ALL_ALLOCATED_PREFIX, LEAF_ALL_ALLOCATED_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.String>(com.cisco.singtel.resourcemanager.base.Converters.confIPv4Converter));
        }
        return this._allAllocated;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: last-allocated</li>
     * <li> Yang type: inet:ipv4-address</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> lastAllocated() {
        if (this._lastAllocated == null) {
            this._lastAllocated = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_LAST_ALLOCATED_PREFIX, LEAF_LAST_ALLOCATED_TAG, com.cisco.singtel.resourcemanager.base.Converters.confIPv4Converter);
        }
        return this._lastAllocated;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList allocatedByConsumer() {
        if (this._allocatedByConsumer == null) {
            this._allocatedByConsumer = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList(this);
        }
        return this._allocatedByConsumer;
    }

    public IpPoolListKey _getKey() {
        return this.key;
    }

    protected void _setKey(IpPoolListKey key) {
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
        this.ranges()._detach();
        this.allAllocated()._unscheduledDelete();
        this.lastAllocated()._unscheduledDelete();
        this.allocatedByConsumer()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList child) {
        if (this._ranges != child) {
            if (this._ranges != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList detached = this._ranges;
                this._ranges = null;
                detached._detach();
            }

            this._ranges = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList detached = this._ranges;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.RangesList)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList child) {
        if (this._allocatedByConsumer != child) {
            if (this._allocatedByConsumer != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList detached = this._allocatedByConsumer;
                this._allocatedByConsumer = null;
                detached._detach();
            }

            this._allocatedByConsumer = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList detached = this._allocatedByConsumer;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ippool.AllocatedByConsumerList)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList detached = this.parent;
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

    public IpPool _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.IpPoolList)null);
        return this;
    }

    public IpPool _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public IpPool _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
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

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.ranges()._configureReader(reader.go("ranges"), channel, scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.allocatedByConsumer()._configureReader(reader.go("allocated-by-consumer"), channel, scope, mode);
            }
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

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.ranges()._collectFromReader(reader.go("ranges"), scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.allocatedByConsumer()._collectFromReader(reader.go("allocated-by-consumer"), scope, mode);
            }
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

    public IpPool _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
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
        this.ranges()._formatTreeTo(buffer, nextIndent, indentStep);
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
        this.allocatedByConsumer()._formatTreeTo(buffer, nextIndent, indentStep);
    }
}
