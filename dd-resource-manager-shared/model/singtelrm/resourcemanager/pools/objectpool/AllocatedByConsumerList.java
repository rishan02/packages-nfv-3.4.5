package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool;


public class AllocatedByConsumerList extends com.cisco.singtel.resourcemanager.base.DataTreeNode implements java.lang.Iterable<AllocatedByConsumer> {
    private static final java.lang.String TAG = "allocated-by-consumer";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "allocated-by-consumer";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool parent;
    private boolean exists = false;
    private final java.util.SortedMap<AllocatedByConsumerListKey, AllocatedByConsumer> elemCache = new java.util.TreeMap<>();

    public AllocatedByConsumerList(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool parent) {
        super(null);
        this.parent = parent;
    }

    public AllocatedByConsumerList(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public AllocatedByConsumer elem(AllocatedByConsumerListKey key) {
        AllocatedByConsumer elem = this.elemCache.get(key);
        if (elem == null) {
            elem = new AllocatedByConsumer(this, key);
            this.elemCache.put(key, elem);
        }
        return elem;
    }

    public AllocatedByConsumer elem(java.lang.String consumer) {
        return this.elem(this.buildKey(consumer));
    }

    public AllocatedByConsumer elem(com.tailf.conf.ConfKey key) {
        return this.elem(this.buildKey(key));
    }

    public AllocatedByConsumer tryElem(AllocatedByConsumerListKey key) {
        return this.elemCache.get(key);
    }

    public AllocatedByConsumer tryElem(java.lang.String consumer) {
        return this.tryElem(this.buildKey(consumer));
    }

    public AllocatedByConsumer tryElem(com.tailf.conf.ConfKey key) {
        return this.tryElem(this.buildKey(key));
    }

    public boolean hasElem(AllocatedByConsumerListKey key) {
        return this.elemCache.containsKey(key);
    }

    public boolean hasElem(java.lang.String consumer) {
        return this.hasElem(this.buildKey(consumer));
    }

    public boolean hasElem(com.tailf.conf.ConfKey key) {
        return this.hasElem(this.buildKey(key));
    }

    public AllocatedByConsumerListKey buildKey(java.lang.String consumer) {
        return new AllocatedByConsumerListKey(consumer);
    }

    public AllocatedByConsumerListKey buildKey(com.tailf.conf.ConfKey key) {
        return new AllocatedByConsumerListKey(key);
    }

    public java.util.List<AllocatedByConsumerListKey> getKeys(se.dataductus.common.nso.channel.MaapiChannel channel) {
        java.util.List<AllocatedByConsumerListKey> keys = new java.util.ArrayList<>();
        java.util.List<com.tailf.conf.ConfKey> confKeys = channel.getListKeys(this._getPath());
        for (com.tailf.conf.ConfKey confKey : confKeys) {
            keys.add(new AllocatedByConsumerListKey(confKey));
        }
        return keys;
    }

    public java.util.Iterator<AllocatedByConsumer> iterator() {
        return this.elemCache.values().iterator();
    }

    public int size() {
        return this.elemCache.size();
    }

    public boolean isEmpty() {
        return this.elemCache.isEmpty();
    }

    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null) {
            super._setPath(parent._getPath().go(TAG));
        }

        return super._getPath();
    }

    public java.lang.String _getLabel() {
        return LABEL;
    }

    public java.lang.String _getPrefix() {
        return PREFIX;
    }

    public java.lang.String _getTag() {
        return TAG;
    }

    public boolean _exists() {
        return this.exists;
    }

    public void _delete() {
        this.exists = false;
        for (AllocatedByConsumerListKey key : elemCache.keySet()) {
            this._detach(key);
        }
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public AllocatedByConsumer _attach(AllocatedByConsumer elem) {
        AllocatedByConsumer cached = this.elemCache.get(elem._getKey());
        if (cached != elem) {
            if (cached != null) {
                this.elemCache.remove(elem._getKey());
                cached._detach();
            }

            this.elemCache.put(elem._getKey(), elem);

            elem._attach(this);
        }

        return elem;
    }

    public AllocatedByConsumer _detach(AllocatedByConsumerListKey key) {
        AllocatedByConsumer elem = this.elemCache.remove(key);
        if (elem != null) {
            elem._detach();
        }
        return elem;
    }

    public AllocatedByConsumer _detach(com.tailf.conf.ConfKey key) {
        return this._detach(this.buildKey(key));
    }

    public AllocatedByConsumer _detach(AllocatedByConsumer elem) {
        AllocatedByConsumer cached = this.elemCache.remove(elem._getKey());
        if (cached == elem) {
            cached._detach();
        }
        else if (cached != null) {
            this.elemCache.put(elem._getKey(), cached);
            cached = null;
        }

        return cached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool detached = this.parent;
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

    public AllocatedByConsumerList _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool)null);
        return this;
    }

    public AllocatedByConsumerList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public AllocatedByConsumerList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            for (com.tailf.conf.ConfKey key : channel.getListKeys(reader)) {
                this.elem(key)._configureReader(reader.elem(key), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            for (java.util.Map.Entry<AllocatedByConsumerListKey, AllocatedByConsumer> entry : this.elemCache.entrySet()) {
                entry.getValue()._collectFromReader(reader.elem(entry.getKey().asConfKey()), scope, mode);
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

    public AllocatedByConsumerList _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("allocated-by-consumer").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("allocated-by-consumer");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    protected java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode> createScheduleComparator() {
        return new java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode>() {
            @Override
            public int compare(com.cisco.singtel.resourcemanager.base.DataNode lhs, com.cisco.singtel.resourcemanager.base.DataNode rhs) {
                return ((AllocatedByConsumer)lhs)._getKey().compareTo(((AllocatedByConsumer)rhs)._getKey());
            }
        };
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        for (AllocatedByConsumer elem : this.elemCache.values()) {
            elem._formatTreeTo(buffer, indent + indentStep, indentStep);
        }
    }
}
