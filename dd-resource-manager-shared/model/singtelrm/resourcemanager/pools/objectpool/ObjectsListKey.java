package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool;


public class ObjectsListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<ObjectsListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.String _object;

    public ObjectsListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public ObjectsListKey(java.lang.String object) {
        this._object = object;
    }


    public java.lang.String object() {
        if (this._object == null) {
            this._object = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertFrom(this.confKey.elementAt(0));
        }

        return this._object;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertTo(this._object);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(ObjectsListKey other) {
        int diff = 0;

        diff = this.object().compareTo(other.object());

        return diff;
    }
}
