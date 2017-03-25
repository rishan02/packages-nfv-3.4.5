package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue;


public class FailedDeviceListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<FailedDeviceListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.String _name;

    public FailedDeviceListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public FailedDeviceListKey(java.lang.String name) {
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
    public int compareTo(FailedDeviceListKey other) {
        int diff = 0;

        diff = this.name().compareTo(other.name());

        return diff;
    }
}
