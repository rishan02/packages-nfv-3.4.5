package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool;


public class ObjectsList extends com.cisco.singtel.resourcemanager.base.DataTreeNode implements java.lang.Iterable<Objects> {
    private static final java.lang.String TAG = "objects";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "objects";
    public static final java.lang.String CALL_POINT_NAME = "ncs-rfs-service-hook";
    public static final java.lang.String SERVICE_POINT_NAME = "vdom-apply-profile";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool parent;
    private boolean exists = false;
    private final java.util.SortedMap<ObjectsListKey, Objects> elemCache = new java.util.TreeMap<>();

    public ObjectsList(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool parent) {
        super(null);
        this.parent = parent;
    }

    public ObjectsList(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public Objects elem(ObjectsListKey key) {
        Objects elem = this.elemCache.get(key);
        if (elem == null) {
            elem = new Objects(this, key);
            this.elemCache.put(key, elem);
        }
        return elem;
    }

    public Objects elem(java.lang.String object) {
        return this.elem(this.buildKey(object));
    }

    public Objects elem(com.tailf.conf.ConfKey key) {
        return this.elem(this.buildKey(key));
    }

    public Objects tryElem(ObjectsListKey key) {
        return this.elemCache.get(key);
    }

    public Objects tryElem(java.lang.String object) {
        return this.tryElem(this.buildKey(object));
    }

    public Objects tryElem(com.tailf.conf.ConfKey key) {
        return this.tryElem(this.buildKey(key));
    }

    public boolean hasElem(ObjectsListKey key) {
        return this.elemCache.containsKey(key);
    }

    public boolean hasElem(java.lang.String object) {
        return this.hasElem(this.buildKey(object));
    }

    public boolean hasElem(com.tailf.conf.ConfKey key) {
        return this.hasElem(this.buildKey(key));
    }

    public ObjectsListKey buildKey(java.lang.String object) {
        return new ObjectsListKey(object);
    }

    public ObjectsListKey buildKey(com.tailf.conf.ConfKey key) {
        return new ObjectsListKey(key);
    }

    public java.util.List<ObjectsListKey> getKeys(se.dataductus.common.nso.channel.MaapiChannel channel) {
        java.util.List<ObjectsListKey> keys = new java.util.ArrayList<>();
        java.util.List<com.tailf.conf.ConfKey> confKeys = channel.getListKeys(this._getPath());
        for (com.tailf.conf.ConfKey confKey : confKeys) {
            keys.add(new ObjectsListKey(confKey));
        }
        return keys;
    }

    public java.util.Iterator<Objects> iterator() {
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
        for (ObjectsListKey key : elemCache.keySet()) {
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

    public Objects _attach(Objects elem) {
        Objects cached = this.elemCache.get(elem._getKey());
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

    public Objects _detach(ObjectsListKey key) {
        Objects elem = this.elemCache.remove(key);
        if (elem != null) {
            elem._detach();
        }
        return elem;
    }

    public Objects _detach(com.tailf.conf.ConfKey key) {
        return this._detach(this.buildKey(key));
    }

    public Objects _detach(Objects elem) {
        Objects cached = this.elemCache.remove(elem._getKey());
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

    public ObjectsList _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.ObjectPool)null);
        return this;
    }

    public ObjectsList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public ObjectsList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
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
            for (java.util.Map.Entry<ObjectsListKey, Objects> entry : this.elemCache.entrySet()) {
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

    public ObjectsList _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("objects").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("objects");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    protected java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode> createScheduleComparator() {
        return new java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode>() {
            @Override
            public int compare(com.cisco.singtel.resourcemanager.base.DataNode lhs, com.cisco.singtel.resourcemanager.base.DataNode rhs) {
                return ((Objects)lhs)._getKey().compareTo(((Objects)rhs)._getKey());
            }
        };
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        for (Objects elem : this.elemCache.values()) {
            elem._formatTreeTo(buffer, indent + indentStep, indentStep);
        }
    }
}
