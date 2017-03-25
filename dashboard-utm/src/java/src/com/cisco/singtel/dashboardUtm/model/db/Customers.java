package com.cisco.singtel.dashboardUtm.model.db;


public class Customers extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "customers";
    private static final java.lang.String PREFIX = "db";
    private static final java.lang.String LABEL = "db:customers";
    private com.cisco.singtel.dashboardUtm.model.Db parent;
    private boolean exists = false;
    private com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList _customer;

    public Customers(com.cisco.singtel.dashboardUtm.model.Db parent) {
        super(null);
        this.parent = parent;
    }

    public Customers(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList customer() {
        if (this._customer == null) {
            this._customer = new com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList(this);
        }
        return this._customer;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
            super._setPath(parent._getPath().go(PREFIX, TAG));
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
        this.customer()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.dashboardUtm.model.Db _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.dashboardUtm.model.Db();
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList _attach(com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList child) {
        if (this._customer != child) {
            if (this._customer != null) {
                com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList detached = this._customer;
                this._customer = null;
                detached._detach();
            }

            this._customer = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList _detach(com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList child) {
        com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList detached = this._customer;
        this._attach((com.cisco.singtel.dashboardUtm.model.db.customers.CustomerList)null);
        return detached;
    }

    public com.cisco.singtel.dashboardUtm.model.Db _attach(com.cisco.singtel.dashboardUtm.model.Db parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.dashboardUtm.model.Db detached = this.parent;
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

    public Customers _detach() {
        this._attach((com.cisco.singtel.dashboardUtm.model.Db)null);
        return this;
    }

    public Customers _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Customers _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.customer()._configureReader(reader.go("customer"), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.customer()._collectFromReader(reader.go("customer"), scope, mode);
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

    public Customers _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("db", "customers").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("db", "customers");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        this.customer()._formatTreeTo(buffer, nextIndent, indentStep);
    }
}
