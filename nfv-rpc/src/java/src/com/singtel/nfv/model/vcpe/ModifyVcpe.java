package com.singtel.nfv.model.vcpe;

/**
 * tailf-info:<br>
 * modify vcpe service<br>
 */
public class ModifyVcpe extends com.cisco.singtel.resourcemanager.base.ActionBase {
    private static final java.lang.String TAG = "modify-vcpe";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:modify-vcpe";
    public static final java.lang.String ACTION_POINT_NAME = "vcpe-modify";
    private com.singtel.nfv.model.Vcpe parent;

    public ModifyVcpe(com.singtel.nfv.model.Vcpe parent) {
        super(null);
        this.parent = parent;
    }

    public ModifyVcpe(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null) {
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

    public com.singtel.nfv.model.vcpe.modifyvcpe.Output request(se.dataductus.common.nso.channel.MaapiChannel channel, com.singtel.nfv.model.vcpe.modifyvcpe.Input input) throws com.tailf.conf.ConfException, java.io.IOException {
        com.tailf.conf.ConfXMLParam[] outputParams = channel.requestActionTh(input._toConfXMLParams(), this._getPath().asKeyPath());
        com.singtel.nfv.model.vcpe.modifyvcpe.Output output = this.newOutput()._fromConfXMLParams(outputParams);
        return output;
    }

    public com.singtel.nfv.model.vcpe.modifyvcpe.Input newInput() {
        return new com.singtel.nfv.model.vcpe.modifyvcpe.Input(this);
    }

    public com.singtel.nfv.model.vcpe.modifyvcpe.Output newOutput() {
        return new com.singtel.nfv.model.vcpe.modifyvcpe.Output(this);
    }

    public com.singtel.nfv.model.vcpe.modifyvcpe.Input parseInput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newInput()._fromConfXMLParams(params);
    }

    public com.singtel.nfv.model.vcpe.modifyvcpe.Output parseOutput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newOutput()._fromConfXMLParams(params);
    }

    public com.singtel.nfv.model.Vcpe _parent() {
        if (this.parent == null) {
            this.parent = new com.singtel.nfv.model.Vcpe();
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.singtel.nfv.model.Vcpe _attach(com.singtel.nfv.model.Vcpe parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.singtel.nfv.model.Vcpe detached = this.parent;
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

    public ModifyVcpe _detach() {
        this._attach((com.singtel.nfv.model.Vcpe)null);
        return this;
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
    }
}
