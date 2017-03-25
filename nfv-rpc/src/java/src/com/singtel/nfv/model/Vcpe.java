package com.singtel.nfv.model;


public class Vcpe extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "vcpe";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:vcpe";
    private com.singtel.nfv.model.vcpe.CreateVcpe _createVcpe;
    private com.singtel.nfv.model.vcpe.ModifyVcpe _modifyVcpe;
    private com.singtel.nfv.model.vcpe.DeleteVcpe _deleteVcpe;
    private com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces _createVcpeSubinterfaces;
    private com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface _deleteVcpeSubinterface;

    public Vcpe() {
        super(se.dataductus.common.nso.path.MaapiPath.goEmpty());
    }

    /**
     * tailf-info:<br>
     * create vcpe service<br>
     */
    public com.singtel.nfv.model.vcpe.CreateVcpe createVcpe() {
        if (this._createVcpe == null) {
            this._createVcpe = new com.singtel.nfv.model.vcpe.CreateVcpe(this);
        }
        return this._createVcpe;
    }

    /**
     * tailf-info:<br>
     * modify vcpe service<br>
     */
    public com.singtel.nfv.model.vcpe.ModifyVcpe modifyVcpe() {
        if (this._modifyVcpe == null) {
            this._modifyVcpe = new com.singtel.nfv.model.vcpe.ModifyVcpe(this);
        }
        return this._modifyVcpe;
    }

    /**
     * tailf-info:<br>
     * delete vcpe service<br>
     */
    public com.singtel.nfv.model.vcpe.DeleteVcpe deleteVcpe() {
        if (this._deleteVcpe == null) {
            this._deleteVcpe = new com.singtel.nfv.model.vcpe.DeleteVcpe(this);
        }
        return this._deleteVcpe;
    }

    /**
     * tailf-info:<br>
     * create vcpe subinterfaces<br>
     */
    public com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces createVcpeSubinterfaces() {
        if (this._createVcpeSubinterfaces == null) {
            this._createVcpeSubinterfaces = new com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces(this);
        }
        return this._createVcpeSubinterfaces;
    }

    /**
     * tailf-info:<br>
     * deletion of vcpe subinterface<br>
     */
    public com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface deleteVcpeSubinterface() {
        if (this._deleteVcpeSubinterface == null) {
            this._deleteVcpeSubinterface = new com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface(this);
        }
        return this._deleteVcpeSubinterface;
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


    public com.singtel.nfv.model.vcpe.CreateVcpe _attach(com.singtel.nfv.model.vcpe.CreateVcpe child) {
        if (this._createVcpe != child) {
            if (this._createVcpe != null) {
                com.singtel.nfv.model.vcpe.CreateVcpe detached = this._createVcpe;
                this._createVcpe = null;
                detached._detach();
            }

            this._createVcpe = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.CreateVcpe _detach(com.singtel.nfv.model.vcpe.CreateVcpe child) {
        com.singtel.nfv.model.vcpe.CreateVcpe detached = this._createVcpe;
        this._attach((com.singtel.nfv.model.vcpe.CreateVcpe)null);
        return detached;
    }

    public com.singtel.nfv.model.vcpe.ModifyVcpe _attach(com.singtel.nfv.model.vcpe.ModifyVcpe child) {
        if (this._modifyVcpe != child) {
            if (this._modifyVcpe != null) {
                com.singtel.nfv.model.vcpe.ModifyVcpe detached = this._modifyVcpe;
                this._modifyVcpe = null;
                detached._detach();
            }

            this._modifyVcpe = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.ModifyVcpe _detach(com.singtel.nfv.model.vcpe.ModifyVcpe child) {
        com.singtel.nfv.model.vcpe.ModifyVcpe detached = this._modifyVcpe;
        this._attach((com.singtel.nfv.model.vcpe.ModifyVcpe)null);
        return detached;
    }

    public com.singtel.nfv.model.vcpe.DeleteVcpe _attach(com.singtel.nfv.model.vcpe.DeleteVcpe child) {
        if (this._deleteVcpe != child) {
            if (this._deleteVcpe != null) {
                com.singtel.nfv.model.vcpe.DeleteVcpe detached = this._deleteVcpe;
                this._deleteVcpe = null;
                detached._detach();
            }

            this._deleteVcpe = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.DeleteVcpe _detach(com.singtel.nfv.model.vcpe.DeleteVcpe child) {
        com.singtel.nfv.model.vcpe.DeleteVcpe detached = this._deleteVcpe;
        this._attach((com.singtel.nfv.model.vcpe.DeleteVcpe)null);
        return detached;
    }

    public com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces _attach(com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces child) {
        if (this._createVcpeSubinterfaces != child) {
            if (this._createVcpeSubinterfaces != null) {
                com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces detached = this._createVcpeSubinterfaces;
                this._createVcpeSubinterfaces = null;
                detached._detach();
            }

            this._createVcpeSubinterfaces = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces _detach(com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces child) {
        com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces detached = this._createVcpeSubinterfaces;
        this._attach((com.singtel.nfv.model.vcpe.CreateVcpeSubinterfaces)null);
        return detached;
    }

    public com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface _attach(com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface child) {
        if (this._deleteVcpeSubinterface != child) {
            if (this._deleteVcpeSubinterface != null) {
                com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface detached = this._deleteVcpeSubinterface;
                this._deleteVcpeSubinterface = null;
                detached._detach();
            }

            this._deleteVcpeSubinterface = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface _detach(com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface child) {
        com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface detached = this._deleteVcpeSubinterface;
        this._attach((com.singtel.nfv.model.vcpe.DeleteVcpeSubinterface)null);
        return detached;
    }

    public Vcpe _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Vcpe _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }
    }

    @Override
    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.DataNode child) {

        return false;
    }

    public Vcpe _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
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
    }
}
