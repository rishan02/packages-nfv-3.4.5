package com.singtel.nfv.model.vcpe.createvcpesubinterfaces.input;


public class WanCvlansListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<WanCvlansListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.Long _wanCvlanId;

    public WanCvlansListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public WanCvlansListKey(java.lang.Long wanCvlanId) {
        this._wanCvlanId = wanCvlanId;
    }


    public java.lang.Long wanCvlanId() {
        if (this._wanCvlanId == null) {
            this._wanCvlanId = com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter.convertFrom(this.confKey.elementAt(0));
        }

        return this._wanCvlanId;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter.convertTo(this._wanCvlanId);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(WanCvlansListKey other) {
        int diff = 0;

        diff = this.wanCvlanId().compareTo(other.wanCvlanId());

        return diff;
    }
}
