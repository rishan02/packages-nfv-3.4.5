package com.cisco.singtel.dashboardUtm.model;


public class Db extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "db";
    private static final java.lang.String PREFIX = "db";
    private static final java.lang.String LABEL = "db:db";
    private com.cisco.singtel.dashboardUtm.model.db.Customers _customers;

    public Db() {
        super(se.dataductus.common.nso.path.MaapiPath.goEmpty());
    }

    public com.cisco.singtel.dashboardUtm.model.db.Customers customers() {
        if (this._customers == null) {
            this._customers = new com.cisco.singtel.dashboardUtm.model.db.Customers(this);
        }
        return this._customers;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null) {
            super._setPath(se.dataductus.common.nso.path.MaapiPath.goEmpty());
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
        return true;
    }


    public com.cisco.singtel.dashboardUtm.model.db.Customers _attach(com.cisco.singtel.dashboardUtm.model.db.Customers child) {
        if (this._customers != child) {
            if (this._customers != null) {
                com.cisco.singtel.dashboardUtm.model.db.Customers detached = this._customers;
                this._customers = null;
                detached._detach();
            }

            this._customers = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.dashboardUtm.model.db.Customers _detach(com.cisco.singtel.dashboardUtm.model.db.Customers child) {
        com.cisco.singtel.dashboardUtm.model.db.Customers detached = this._customers;
        this._attach((com.cisco.singtel.dashboardUtm.model.db.Customers)null);
        return detached;
    }

    public Db _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Db _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this.customers()._read(channel, scope);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {

            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.customers()._configureReader(reader.go("db", "customers"), channel, scope, mode);
            }
        }
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.customers()._collectFromReader(reader.go("db", "customers"), scope, mode);
            }
        }
    }

    @Override
    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.DataNode child) {

        return false;
    }

    public Db _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
            child._configureWriter(parentWriter);
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.customers()._exists()) {
            this.customers()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
