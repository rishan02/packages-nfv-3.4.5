package com.singtel.nfv.model.vcpe.createvcpe.input;


/**
 * tailf-info:<br>
 * Ingress VLAN subinterfaces<br>
 */
public class LanCvlansList extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode implements java.lang.Iterable<LanCvlans> {
    private static final java.lang.String TAG = "lan-cvlans";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "lan-cvlans";
    private com.singtel.nfv.model.vcpe.createvcpe.Input parent;
    private boolean exists = false;
    private final java.util.SortedMap<LanCvlansListKey, LanCvlans> elemCache = new java.util.TreeMap<>();

    public LanCvlansList(com.singtel.nfv.model.vcpe.createvcpe.Input parent) {
        super(null);
        this.parent = parent;
    }

    public LanCvlansList(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public LanCvlans elem(LanCvlansListKey key) {
        LanCvlans elem = this.elemCache.get(key);
        if (elem == null) {
            elem = new LanCvlans(this, key);
            this.elemCache.put(key, elem);
        }
        return elem;
    }

    public LanCvlans elem(java.lang.Long lanCvlanId) {
        return this.elem(this.buildKey(lanCvlanId));
    }

    public LanCvlans elem(com.tailf.conf.ConfKey key) {
        return this.elem(this.buildKey(key));
    }

    public LanCvlans tryElem(LanCvlansListKey key) {
        return this.elemCache.get(key);
    }

    public LanCvlans tryElem(java.lang.Long lanCvlanId) {
        return this.tryElem(this.buildKey(lanCvlanId));
    }

    public LanCvlans tryElem(com.tailf.conf.ConfKey key) {
        return this.tryElem(this.buildKey(key));
    }

    public boolean hasElem(LanCvlansListKey key) {
        return this.elemCache.containsKey(key);
    }

    public boolean hasElem(java.lang.Long lanCvlanId) {
        return this.hasElem(this.buildKey(lanCvlanId));
    }

    public boolean hasElem(com.tailf.conf.ConfKey key) {
        return this.hasElem(this.buildKey(key));
    }

    public LanCvlansListKey buildKey(java.lang.Long lanCvlanId) {
        return new LanCvlansListKey(lanCvlanId);
    }

    public LanCvlansListKey buildKey(com.tailf.conf.ConfKey key) {
        return new LanCvlansListKey(key);
    }

    public java.util.List<LanCvlansListKey> getKeys(se.dataductus.common.nso.channel.MaapiChannel channel) {
        java.util.List<LanCvlansListKey> keys = new java.util.ArrayList<>();
        java.util.List<com.tailf.conf.ConfKey> confKeys = channel.getListKeys(this._getPath());
        for (com.tailf.conf.ConfKey confKey : confKeys) {
            keys.add(new LanCvlansListKey(confKey));
        }
        return keys;
    }

    public java.util.Iterator<LanCvlans> iterator() {
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
        for (LanCvlansListKey key : elemCache.keySet()) {
            this._detach(key);
        }
        this.parent._scheduleWrite(this);
    }

    public com.singtel.nfv.model.vcpe.createvcpe.Input _parent() {
        if (this.parent == null) {
            this.parent = new com.singtel.nfv.model.vcpe.createvcpe.Input(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public LanCvlans _attach(LanCvlans elem) {
        LanCvlans cached = this.elemCache.get(elem._getKey());
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

    public LanCvlans _detach(LanCvlansListKey key) {
        LanCvlans elem = this.elemCache.remove(key);
        if (elem != null) {
            elem._detach();
        }
        return elem;
    }

    public LanCvlans _detach(com.tailf.conf.ConfKey key) {
        return this._detach(this.buildKey(key));
    }

    public LanCvlans _detach(LanCvlans elem) {
        LanCvlans cached = this.elemCache.remove(elem._getKey());
        if (cached == elem) {
            cached._detach();
        }
        else if (cached != null) {
            this.elemCache.put(elem._getKey(), cached);
            cached = null;
        }

        return cached;
    }

    public com.singtel.nfv.model.vcpe.createvcpe.Input _attach(com.singtel.nfv.model.vcpe.createvcpe.Input parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.singtel.nfv.model.vcpe.createvcpe.Input detached = this.parent;
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

    public LanCvlansList _detach() {
        this._attach((com.singtel.nfv.model.vcpe.createvcpe.Input)null);
        return this;
    }

    public int _parseConfXMLParams(com.tailf.conf.ConfXMLParam[] params, int start, int end) throws com.tailf.conf.ConfException {
        LanCvlans elem = new LanCvlans();
        int nextStart = elem._parseConfXMLParams(params, start, end);
        elem._attach(this);

        this.exists = true;

        return nextStart;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        for (LanCvlans child : this.elemCache.values()) {
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
        for (LanCvlans elem : this.elemCache.values()) {
            elem._formatTreeTo(buffer, indent + indentStep, indentStep);
        }
    }
}
