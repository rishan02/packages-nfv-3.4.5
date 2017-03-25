package com.cisco.singtel.dashboardUtm.model.db.customers;


public class CustomerListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<CustomerListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.String _brn;

    public CustomerListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public CustomerListKey(java.lang.String brn) {
        this._brn = brn;
    }


    public java.lang.String brn() {
        if (this._brn == null) {
            this._brn = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertFrom(this.confKey.elementAt(0));
        }

        return this._brn;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertTo(this._brn);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(CustomerListKey other) {
        int diff = 0;

        diff = this.brn().compareTo(other.brn());

        return diff;
    }
}
