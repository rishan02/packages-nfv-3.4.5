package com.cisco.singtel.resourcemanager.model.singtelrm;


public class ResourceManager extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "resource-manager";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "singtel-rm:resource-manager";
    private com.cisco.singtel.resourcemanager.model.SingtelRm parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools _pools;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus _vnfStatus;

    public ResourceManager(com.cisco.singtel.resourcemanager.model.SingtelRm parent) {
        super(null);
        this.parent = parent;
    }

    public ResourceManager(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools pools() {
        if (this._pools == null) {
            this._pools = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools(this);
        }
        return this._pools;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus vnfStatus() {
        if (this._vnfStatus == null) {
            this._vnfStatus = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus(this);
        }
        return this._vnfStatus;
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
        this.pools()._detach();
        this.vnfStatus()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.SingtelRm _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.SingtelRm();
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools child) {
        if (this._pools != child) {
            if (this._pools != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools detached = this._pools;
                this._pools = null;
                detached._detach();
            }

            this._pools = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools detached = this._pools;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.Pools)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus child) {
        if (this._vnfStatus != child) {
            if (this._vnfStatus != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus detached = this._vnfStatus;
                this._vnfStatus = null;
                detached._detach();
            }

            this._vnfStatus = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus detached = this._vnfStatus;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.VnfStatus)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.SingtelRm _attach(com.cisco.singtel.resourcemanager.model.SingtelRm parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.SingtelRm detached = this.parent;
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

    public ResourceManager _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.SingtelRm)null);
        return this;
    }

    public ResourceManager _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public ResourceManager _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {

            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG) || mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.pools()._configureReader(reader.go("pools"), channel, scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.vnfStatus()._configureReader(reader.go("vnf-status"), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG) || mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.pools()._collectFromReader(reader.go("pools"), scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.vnfStatus()._collectFromReader(reader.go("vnf-status"), scope, mode);
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

    public ResourceManager _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("singtel-rm", "resource-manager").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("singtel-rm", "resource-manager");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.pools()._exists()) {
            this.pools()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.vnfStatus()._exists()) {
            this.vnfStatus()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
