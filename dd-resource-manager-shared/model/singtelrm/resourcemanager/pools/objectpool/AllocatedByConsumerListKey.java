package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool;


public class AllocatedByConsumerListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<AllocatedByConsumerListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.String _consumer;

    public AllocatedByConsumerListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public AllocatedByConsumerListKey(java.lang.String consumer) {
        this._consumer = consumer;
    }


    public java.lang.String consumer() {
        if (this._consumer == null) {
            this._consumer = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertFrom(this.confKey.elementAt(0));
        }

        return this._consumer;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertTo(this._consumer);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(AllocatedByConsumerListKey other) {
        int diff = 0;

        diff = this.consumer().compareTo(other.consumer());

        return diff;
    }
}
