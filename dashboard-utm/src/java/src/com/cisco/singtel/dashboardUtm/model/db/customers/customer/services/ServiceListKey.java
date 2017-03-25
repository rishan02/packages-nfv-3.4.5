package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services;


public class ServiceListKey extends com.cisco.singtel.resourcemanager.base.KeyBase implements Comparable<ServiceListKey> {
    private com.tailf.conf.ConfKey confKey;

    private java.lang.String _nfvVasReference;

    public ServiceListKey(com.tailf.conf.ConfKey confKey) {
        this.confKey = confKey;
    }

    public ServiceListKey(java.lang.String nfvVasReference) {
        this._nfvVasReference = nfvVasReference;
    }


    public java.lang.String nfvVasReference() {
        if (this._nfvVasReference == null) {
            this._nfvVasReference = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertFrom(this.confKey.elementAt(0));
        }

        return this._nfvVasReference;
    }

    public com.tailf.conf.ConfKey asConfKey() {
        if (this.confKey == null) {
            com.tailf.conf.ConfObject[] elems = new com.tailf.conf.ConfObject[1];
            try {
                elems[0] = com.cisco.singtel.resourcemanager.base.Converters.confBufConverter.convertTo(this._nfvVasReference);
            } catch (com.tailf.conf.ConfException e) {}
            this.confKey = new com.tailf.conf.ConfKey(elems);
        }

        return this.confKey;
    }

    @Override
    public int compareTo(ServiceListKey other) {
        int diff = 0;

        diff = this.nfvVasReference().compareTo(other.nfvVasReference());

        return diff;
    }
}
