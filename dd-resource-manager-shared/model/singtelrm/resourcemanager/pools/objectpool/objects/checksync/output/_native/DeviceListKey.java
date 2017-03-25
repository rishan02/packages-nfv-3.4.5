package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.output._native;


public class DeviceListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<DeviceListKey> {
    private com.tailf.conf.ConfKey confKey;


    public DeviceListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public DeviceListKey() {
    }


    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[0];
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(DeviceListKey other) {
        int diff = 0;

        return diff;
    }
}
