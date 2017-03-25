package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects;

/**
 * Description:<br>
 * Run the service code again, possibly writing the changes of<br>
 * the service to the network once again. If the dryrun option is used,<br>
 * the action simply reports (in different formats) what it would do.<br>
 *  Use the option 'reconcile' if the service should reconciliate<br>
 * original data, i.e., take control of that data. This option<br>
 * acknowledges other services controlling the same data. The<br>
 * reference count will indicate how many services control the<br>
 * data.<br>
 * <br>
 * tailf-info:<br>
 * Run/Dryrun the service logic again<br>
 */
public class ReDeploy extends com.cisco.singtel.resourcemanager.base.ActionBase {
    private static final java.lang.String TAG = "re-deploy";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "re-deploy";
    public static final java.lang.String ACTION_POINT_NAME = "ncsinternal";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent;

    public ReDeploy(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        super(null);
        this.parent = parent;
    }

    public ReDeploy(se.dataductus.common.nso.path.MaapiPath path) {
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Output request(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input input) throws com.tailf.conf.ConfException, java.io.IOException {
        com.tailf.conf.ConfXMLParam[] outputParams = channel.requestActionTh(input._toConfXMLParams(), this._getPath().asKeyPath());
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Output output = this.newOutput()._fromConfXMLParams(outputParams);
        return output;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input newInput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Output newOutput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Output(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input parseInput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newInput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Output parseOutput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
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

    public ReDeploy _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects)null);
        return this;
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
    }
}
