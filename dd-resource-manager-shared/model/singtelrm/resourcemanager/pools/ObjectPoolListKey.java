package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools;


public class ObjectPoolListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<ObjectPoolListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.String _name;

    public ObjectPoolListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public ObjectPoolListKey(java.lang.String name) {
        this._name = name;
    }


    public java.lang.String name() {
        if (this._name == null) {
            this._name = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertFrom(this.confKey.elementAt(0));
        }

        return this._name;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertTo(this._name);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(ObjectPoolListKey other) {
        int diff = 0;

        diff = this.name().compareTo(other.name());

        return diff;
    }
}
