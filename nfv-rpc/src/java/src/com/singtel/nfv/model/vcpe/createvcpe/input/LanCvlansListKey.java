package com.singtel.nfv.model.vcpe.createvcpe.input;


public class LanCvlansListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<LanCvlansListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.Long _lanCvlanId;

    public LanCvlansListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public LanCvlansListKey(java.lang.Long lanCvlanId) {
        this._lanCvlanId = lanCvlanId;
    }


    public java.lang.Long lanCvlanId() {
        if (this._lanCvlanId == null) {
            this._lanCvlanId = com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter.convertFrom(this.confKey.elementAt(0));
        }

        return this._lanCvlanId;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter.convertTo(this._lanCvlanId);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(LanCvlansListKey other) {
        int diff = 0;

        diff = this.lanCvlanId().compareTo(other.lanCvlanId());

        return diff;
    }
}
