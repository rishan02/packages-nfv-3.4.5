package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools;


public class ObjectPool extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "object-pool";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "object-pool";
    public static final java.lang.String LEAF_NAME_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NAME_TAG = "name";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList parent;
    private boolean exists = false;
    private ObjectPoolListKey key;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _name;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList _objects;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList _allocatedByConsumer;

    public ObjectPool(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList parent, ObjectPoolListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public ObjectPool(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new ObjectPoolListKey(path.getKey()));
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList objects() {
        if (this._objects == null) {
            this._objects = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList(this);
        }
        return this._objects;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList allocatedByConsumer() {
        if (this._allocatedByConsumer == null) {
            this._allocatedByConsumer = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList(this);
        }
        return this._allocatedByConsumer;
    }

    public ObjectPoolListKey _getKey() {
        return this.key;
    }

    protected void _setKey(ObjectPoolListKey key) {
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
        this.objects()._detach();
        this.allocatedByConsumer()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList child) {
        if (this._objects != child) {
            if (this._objects != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList detached = this._objects;
                this._objects = null;
                detached._detach();
            }

            this._objects = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList detached = this._objects;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList child) {
        if (this._allocatedByConsumer != child) {
            if (this._allocatedByConsumer != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList detached = this._allocatedByConsumer;
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList detached = this._allocatedByConsumer;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.AllocatedByConsumerList)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList detached = this.parent;
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

    public ObjectPool _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPoolList)null);
        return this;
    }

    public ObjectPool _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public ObjectPool _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG) || mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.objects()._configureReader(reader.go("objects"), channel, scope, mode);
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

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG) || mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.objects()._collectFromReader(reader.go("objects"), scope, mode);
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

    public ObjectPool _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
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
        this.objects()._formatTreeTo(buffer, nextIndent, indentStep);
        this.allocatedByConsumer()._formatTreeTo(buffer, nextIndent, indentStep);
    }
}
