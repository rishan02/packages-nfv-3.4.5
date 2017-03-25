package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects;

/**
 * Description:<br>
 * Check if this service has been undermined on the device itself.<br>
 * The action 'check-sync' compares the output of the service<br>
 * code to what is stored in CDB locally. This action retrieves the<br>
 * configuration from the devices touched by the service and compares<br>
 * the forward diff set of the service to the retrieved data. This<br>
 * is thus a fairly heavy weight operation. As opposed to the check-sync<br>
 * action that invokes the FASTMAP code, this action re-applies the<br>
 * forward diff-set. This is the same output you see when inspecting<br>
 * the 'get-modifications' operational field in the service instance.<br>
 *  If the device is in sync with CDB, the output of this action<br>
 * is identical to the output of the cheaper check-sync action<br>
 * <br>
 * tailf-info:<br>
 * Check if device config is according to the service<br>
 */
public class DeepCheckSync extends com.cisco.singtel.resourcemanager.base.ActionBase {
    private static final java.lang.String TAG = "deep-check-sync";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "deep-check-sync";
    public static final java.lang.String ACTION_POINT_NAME = "ncsinternal";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent;

    public DeepCheckSync(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        super(null);
        this.parent = parent;
    }

    public DeepCheckSync(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null) {
            super._setPath(parent._getPath().go(TAG));
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Output request(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Input input) throws com.tailf.conf.ConfException, java.io.IOException {
        com.tailf.conf.ConfXMLParam[] outputParams = channel.requestActionTh(input._toConfXMLParams(), this._getPath().asKeyPath());
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Output output = this.newOutput()._fromConfXMLParams(outputParams);
        return output;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Input newInput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Input(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Output newOutput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Output(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Input parseInput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newInput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync.Output parseOutput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newOutput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects detached = this.parent;
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

    public DeepCheckSync _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects)null);
        return this;
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
    }
}
