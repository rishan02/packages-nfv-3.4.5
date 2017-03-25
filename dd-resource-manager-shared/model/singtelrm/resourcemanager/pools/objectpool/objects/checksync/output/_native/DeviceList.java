package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output._native;


public class DeviceList extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode implements java.lang.Iterable<Device> {
    private static final java.lang.String TAG = "device";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "device";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native parent;
    private boolean exists = false;
    private final java.util.SortedMap<DeviceListKey, Device> elemCache = new java.util.TreeMap<>();

    public DeviceList(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native parent) {
        super(null);
        this.parent = parent;
    }

    public DeviceList(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public Device elem(DeviceListKey key) {
        Device elem = this.elemCache.get(key);
        if (elem == null) {
            elem = new Device(this, key);
            this.elemCache.put(key, elem);
        }
        return elem;
    }

    public Device elem() {
        return this.elem(this.buildKey());
    }

    public Device elem(com.tailf.conf.ConfKey key) {
        return this.elem(this.buildKey(key));
    }

    public Device tryElem(DeviceListKey key) {
        return this.elemCache.get(key);
    }

    public Device tryElem() {
        return this.tryElem(this.buildKey());
    }

    public Device tryElem(com.tailf.conf.ConfKey key) {
        return this.tryElem(this.buildKey(key));
    }

    public boolean hasElem(DeviceListKey key) {
        return this.elemCache.containsKey(key);
    }

    public boolean hasElem() {
        return this.hasElem(this.buildKey());
    }

    public boolean hasElem(com.tailf.conf.ConfKey key) {
        return this.hasElem(this.buildKey(key));
    }

    public DeviceListKey buildKey() {
        return new DeviceListKey();
    }

    public DeviceListKey buildKey(com.tailf.conf.ConfKey key) {
        return new DeviceListKey(key);
    }

    public java.util.List<DeviceListKey> getKeys(se.dataductus.common.nso.channel.MaapiChannel channel) {
        java.util.List<DeviceListKey> keys = new java.util.ArrayList<>();
        java.util.List<com.tailf.conf.ConfKey> confKeys = channel.getListKeys(this._getPath());
        for (com.tailf.conf.ConfKey confKey : confKeys) {
            keys.add(new DeviceListKey(confKey));
        }
        return keys;
    }

    public java.util.Iterator<Device> iterator() {
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
        for (DeviceListKey key : elemCache.keySet()) {
            this._detach(key);
        }
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public Device _attach(Device elem) {
        Device cached = this.elemCache.get(elem._getKey());
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

    public Device _detach(DeviceListKey key) {
        Device elem = this.elemCache.remove(key);
        if (elem != null) {
            elem._detach();
        }
        return elem;
    }

    public Device _detach(com.tailf.conf.ConfKey key) {
        return this._detach(this.buildKey(key));
    }

    public Device _detach(Device elem) {
        Device cached = this.elemCache.remove(elem._getKey());
        if (cached == elem) {
            cached._detach();
        }
        else if (cached != null) {
            this.elemCache.put(elem._getKey(), cached);
            cached = null;
        }

        return cached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native detached = this.parent;
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

    public DeviceList _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output.Native)null);
        return this;
    }

    public int _parseConfXMLParams(com.tailf.conf.ConfXMLParam[] params, int start, int end) throws com.tailf.conf.ConfException {
        Device elem = new Device();
        int nextStart = elem._parseConfXMLParams(params, start, end);
        elem._attach(this);

        this.exists = true;

        return nextStart;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        for (Device child : this.elemCache.values()) {
            child._writeConfXMLParams(params);
        }
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

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        for (Device elem : this.elemCache.values()) {
            elem._formatTreeTo(buffer, indent + indentStep, indentStep);
        }
    }
}
