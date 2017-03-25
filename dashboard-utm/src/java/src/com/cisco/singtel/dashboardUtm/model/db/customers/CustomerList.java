package com.cisco.singtel.dashboardUtm.model.db.customers;


public class CustomerList extends com.cisco.singtel.resourcemanager.base.DataTreeNode implements java.lang.Iterable<Customer> {
    private static final java.lang.String TAG = "customer";
    private static final java.lang.String PREFIX = "db";
    private static final java.lang.String LABEL = "customer";
    private com.cisco.singtel.dashboardUtm.model.db.Customers parent;
    private boolean exists = false;
    private final java.util.SortedMap<CustomerListKey, Customer> elemCache = new java.util.TreeMap<>();

    public CustomerList(com.cisco.singtel.dashboardUtm.model.db.Customers parent) {
        super(null);
        this.parent = parent;
    }

    public CustomerList(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public Customer elem(CustomerListKey key) {
        Customer elem = this.elemCache.get(key);
        if (elem == null) {
            elem = new Customer(this, key);
            this.elemCache.put(key, elem);
        }
        return elem;
    }

    public Customer elem(java.lang.String brn) {
        return this.elem(this.buildKey(brn));
    }

    public Customer elem(com.tailf.conf.ConfKey key) {
        return this.elem(this.buildKey(key));
    }

    public Customer tryElem(CustomerListKey key) {
        return this.elemCache.get(key);
    }

    public Customer tryElem(java.lang.String brn) {
        return this.tryElem(this.buildKey(brn));
    }

    public Customer tryElem(com.tailf.conf.ConfKey key) {
        return this.tryElem(this.buildKey(key));
    }

    public boolean hasElem(CustomerListKey key) {
        return this.elemCache.containsKey(key);
    }

    public boolean hasElem(java.lang.String brn) {
        return this.hasElem(this.buildKey(brn));
    }

    public boolean hasElem(com.tailf.conf.ConfKey key) {
        return this.hasElem(this.buildKey(key));
    }

    public CustomerListKey buildKey(java.lang.String brn) {
        return new CustomerListKey(brn);
    }

    public CustomerListKey buildKey(com.tailf.conf.ConfKey key) {
        return new CustomerListKey(key);
    }

    public java.util.List<CustomerListKey> getKeys(se.dataductus.common.nso.channel.MaapiChannel channel) {
        java.util.List<CustomerListKey> keys = new java.util.ArrayList<>();
        java.util.List<com.tailf.conf.ConfKey> confKeys = channel.getListKeys(this._getPath());
        for (com.tailf.conf.ConfKey confKey : confKeys) {
            keys.add(new CustomerListKey(confKey));
        }
        return keys;
    }

    public java.util.Iterator<Customer> iterator() {
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
        for (CustomerListKey key : elemCache.keySet()) {
            this._detach(key);
        }
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.dashboardUtm.model.db.Customers _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.dashboardUtm.model.db.Customers(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public Customer _attach(Customer elem) {
        Customer cached = this.elemCache.get(elem._getKey());
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

    public Customer _detach(CustomerListKey key) {
        Customer elem = this.elemCache.remove(key);
        if (elem != null) {
            elem._detach();
        }
        return elem;
    }

    public Customer _detach(com.tailf.conf.ConfKey key) {
        return this._detach(this.buildKey(key));
    }

    public Customer _detach(Customer elem) {
        Customer cached = this.elemCache.remove(elem._getKey());
        if (cached == elem) {
            cached._detach();
        }
        else if (cached != null) {
            this.elemCache.put(elem._getKey(), cached);
            cached = null;
        }

        return cached;
    }

    public com.cisco.singtel.dashboardUtm.model.db.Customers _attach(com.cisco.singtel.dashboardUtm.model.db.Customers parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.dashboardUtm.model.db.Customers detached = this.parent;
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

    public CustomerList _detach() {
        this._attach((com.cisco.singtel.dashboardUtm.model.db.Customers)null);
        return this;
    }

    public CustomerList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public CustomerList _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            for (com.tailf.conf.ConfKey key : channel.getListKeys(reader)) {
                this.elem(key)._configureReader(reader.elem(key), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            for (java.util.Map.Entry<CustomerListKey, Customer> entry : this.elemCache.entrySet()) {
                entry.getValue()._collectFromReader(reader.elem(entry.getKey().asConfKey()), scope, mode);
            }
        }
    }

    @Override
    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.DataNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        if (super._scheduleWrite(child) && parent != null) {
            parent._scheduleWrite(this);
            return true;
        }

        return false;
    }

    public CustomerList _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("customer").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("customer");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    protected java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode> createScheduleComparator() {
        return new java.util.Comparator<com.cisco.singtel.resourcemanager.base.DataNode>() {
            @Override
            public int compare(com.cisco.singtel.resourcemanager.base.DataNode lhs, com.cisco.singtel.resourcemanager.base.DataNode rhs) {
                return ((Customer)lhs)._getKey().compareTo(((Customer)rhs)._getKey());
            }
        };
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        for (Customer elem : this.elemCache.values()) {
            elem._formatTreeTo(buffer, indent + indentStep, indentStep);
        }
    }
}
