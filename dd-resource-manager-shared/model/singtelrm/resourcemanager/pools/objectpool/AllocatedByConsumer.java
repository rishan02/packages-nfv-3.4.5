package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool;


public class AllocatedByConsumer extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "allocated-by-consumer";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "allocated-by-consumer";
    public static final java.lang.String LEAF_CONSUMER_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_CONSUMER_TAG = "consumer";
    public static final java.lang.String LEAF_ALLOCATED_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_ALLOCATED_TAG = "allocated";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList parent;
    private boolean exists = false;
    private AllocatedByConsumerListKey key;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _consumer;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _allocated;

    public AllocatedByConsumer(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList parent, AllocatedByConsumerListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public AllocatedByConsumer(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new AllocatedByConsumerListKey(path.getKey()));
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: consumer</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> consumer() {
        if (this._consumer == null) {
            this._consumer = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_CONSUMER_PREFIX, LEAF_CONSUMER_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._consumer;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: allocated</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> allocated() {
        if (this._allocated == null) {
            this._allocated = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_ALLOCATED_PREFIX, LEAF_ALLOCATED_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._allocated;
    }

    public AllocatedByConsumerListKey _getKey() {
        return this.key;
    }

    protected void _setKey(AllocatedByConsumerListKey key) {
        this.consumer()._unscheduledSet(key.consumer());

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
        this.consumer()._unscheduledDelete();
        this.allocated()._unscheduledDelete();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList detached = this.parent;
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

    public AllocatedByConsumer _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList)null);
        return this;
    }

    public AllocatedByConsumer _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public AllocatedByConsumer _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            this.allocated()._configureReader(reader);

        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            this.allocated()._collectFromReader(reader);

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

    public AllocatedByConsumer _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
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
        if (this.consumer().exists()) {
            buffer.append(nextIndent).append("consumer").append(": ").append(this.consumer().get()).append(java.lang.System.lineSeparator());
        }
        if (this.allocated().exists()) {
            buffer.append(nextIndent).append("allocated").append(": ").append(this.allocated().get()).append(java.lang.System.lineSeparator());
        }
    }
}
