package com.cisco.singtel.dashboardUtm.model.db.customers;


public class Customer extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "customer";
    private static final java.lang.String PREFIX = "db";
    private static final java.lang.String LABEL = "customer";
    public static final java.lang.String LEAF_BRN_PREFIX = "db";
    public static final java.lang.String LEAF_BRN_TAG = "brn";
    public static final java.lang.String LEAF_CUSTOMER_NAME_PREFIX = "db";
    public static final java.lang.String LEAF_CUSTOMER_NAME_TAG = "customer-name";
    private com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList parent;
    private boolean exists = false;
    private CustomerListKey key;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _brn;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _customerName;
    private com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services _services;

    public Customer(com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList parent, CustomerListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public Customer(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new CustomerListKey(path.getKey()));
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: brn</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> brn() {
        if (this._brn == null) {
            this._brn = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_BRN_PREFIX, LEAF_BRN_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._brn;
    }

    /**
     * tailf-info:<br>
     * Name of customer<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: customer-name</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> customerName() {
        if (this._customerName == null) {
            this._customerName = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_CUSTOMER_NAME_PREFIX, LEAF_CUSTOMER_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._customerName;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services services() {
        if (this._services == null) {
            this._services = new com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services(this);
        }
        return this._services;
    }

    public CustomerListKey _getKey() {
        return this.key;
    }

    protected void _setKey(CustomerListKey key) {
        this.brn()._unscheduledSet(key.brn());

        this.key = key;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
            super._setPath(parent._getPath().elem(this._getKey().asConfKey()));
        }

        return super._getPath();
    }

    @Override
    public java.lang.String _getLabel() {
        return LABEL;
    }

    @Override
    public java.lang.String _getPrefix() {
        return PREFIX;
    }

    @Override
    public java.lang.String _getTag() {
        return TAG;
    }

    public boolean _exists() {
        return this.exists;
    }

    public void _create() {
        this.exists = true;
        this.parent._scheduleWrite(this);
    }

    public void _delete() {
        this.exists = false;
        this.brn()._unscheduledDelete();
        this.customerName()._unscheduledDelete();
        this.services()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services _attach(com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services child) {
        if (this._services != child) {
            if (this._services != null) {
                com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services detached = this._services;
                this._services = null;
                detached._detach();
            }

            this._services = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services _detach(com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services child) {
        com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services detached = this._services;
        this._attach((com.cisco.singtel.dashboardUtm.model.db.customers.customer.Services)null);
        return detached;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList _attach(com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList detached = this.parent;
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

    public Customer _detach() {
        this._attach((com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList)null);
        return this;
    }

    public Customer _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Customer _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.customerName()._configureReader(reader);

        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {

            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.services()._configureReader(reader.go("services"), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.customerName()._collectFromReader(reader);

        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.services()._collectFromReader(reader.go("services"), scope, mode);
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

    public Customer _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.elem(this._getKey().asConfKey()).setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.elem(this._getKey().asConfKey());
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.brn().exists()) {
            buffer.append(nextIndent).append("brn").append(": ").append(this.brn().get()).append(java.lang.System.lineSeparator());
        }
        if (this.customerName().exists()) {
            buffer.append(nextIndent).append("customerName").append(": ").append(this.customerName().get()).append(java.lang.System.lineSeparator());
        }
        if (this.services()._exists()) {
            this.services()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
