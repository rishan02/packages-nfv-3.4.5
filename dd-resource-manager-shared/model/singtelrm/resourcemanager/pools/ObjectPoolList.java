package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools;


public class ObjectPoolList extends com.cisco.singtel.resourcemanager.base.DataTreeNode implements java.lang.Iterable<ObjectPool> {
    private static final java.lang.String TAG = "object-pool";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "object-pool";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools parent;
    private boolean exists = false;
    private final java.util.SortedMap<ObjectPoolListKey, ObjectPool> elemCache = new java.util.TreeMap<>();

    public ObjectPoolList(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools parent) {
        super(null);
        this.parent = parent;
    }

    public ObjectPoolList(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public ObjectPool elem(ObjectPoolListKey key) {
        ObjectPool elem = this.elemCache.get(key);
        if (elem == null) {
            elem = new ObjectPool(this, key);
            this.elemCache.put(key, elem);
        }
        return elem;
    }

    public ObjectPool elem(java.lang.String name) {
        return this.elem(this.buildKey(name));
    }

    public ObjectPool elem(com.tailf.conf.ConfKey key) {
        return this.elem(this.buildKey(key));
    }

    public ObjectPool tryElem(ObjectPoolListKey key) {
        return this.elemCache.get(key);
    }

    public ObjectPool tryElem(java.lang.String name) {
        return this.tryElem(this.buildKey(name));
    }

    public ObjectPool tryElem(com.tailf.conf.ConfKey key) {
        return this.tryElem(this.buildKey(key));
    }

    public boolean hasElem(ObjectPoolListKey key) {
        return this.elemCache.containsKey(key);
    }

    public boolean hasElem(java.lang.String name) {
        return this.hasElem(this.buildKey(name));
    }

    public boolean hasElem(com.tailf.conf.ConfKey key) {
        return this.hasElem(this.buildKey(key));
    }

    public ObjectPoolListKey buildKey(java.lang.String name) {
        return new ObjectPoolListKey(name);
    }

    public ObjectPoolListKey buildKey(com.tailf.conf.ConfKey key) {
        return new ObjectPoolListKey(key);
    }

    public java.util.List<ObjectPoolListKey> getKeys(se.dataductus.common.nso.channel.MaapiChannel channel) {
        java.util.List<ObjectPoolListKey> keys = new java.util.ArrayList<>();
        java.util.List<com.tailf.conf.ConfKey> confKeys = channel.getListKeys(this._getPath());
        for (com.tailf.conf.ConfKey confKey : confKeys) {
            keys.add(new ObjectPoolListKey(confKey));
        }
        return keys;
    }

    public java.util.Iterator<ObjectPool> iterator() {
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
        for (ObjectPoolListKey key : elemCache.keySet()) {
            this._detach(key);
        }
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

    public ObjectPool _attach(ObjectPool elem) {
        ObjectPool cached = this.elemCache.get(elem._getKey());
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

    public ObjectPool _detach(ObjectPoolListKey key) {
        ObjectPool elem = this.elemCache.remove(key);
        if (elem != null) {
            elem._detach();
        }
        return elem;
    }

    public ObjectPool _detach(com.tailf.conf.ConfKey key) {
        return this._detach(this.buildKey(key));
    }

    public ObjectPool _detach(ObjectPool elem) {
        ObjectPool cached = this.elemCache.remove(elem._getKey());
        if (cached == elem) {
            cached._detach();
        }
        else if (cached != null) {
            this.elemCache.put(elem._getKey(), cached);
            cached = null;
        }

        return cached;
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

    public ObjectPoolList _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools)null);
        return this;
    }

    public ObjectPoolList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public ObjectPoolList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
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
        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG) || mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            for (java.util.Map.Entry<ObjectPoolListKey, ObjectPool> entry : this.elemCache.entrySet()) {
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

    public ObjectPoolList _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("object-pool").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("object-pool");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    protected java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode> createScheduleComparator() {
        return new java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode>() {
            @Override
            public int compare(com.cisco.singtel.resourcemanager.base.DataNode lhs, com.cisco.singtel.resourcemanager.base.DataNode rhs) {
                return ((ObjectPool)lhs)._getKey().compareTo(((ObjectPool)rhs)._getKey());
            }
        };
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        for (ObjectPool elem : this.elemCache.values()) {
            elem._formatTreeTo(buffer, indent + indentStep, indentStep);
        }
    }
}
