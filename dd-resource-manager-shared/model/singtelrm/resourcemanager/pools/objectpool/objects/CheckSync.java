package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects;

/**
 * Description:<br>
 * Check if this service has been undermined, i.e., if this service<br>
 * was to be re-deployed, would it do anything. This action will<br>
 * invoke the FASTMAP code to create the change set that is compared<br>
 * to the existing data in CDB locally.<br>
 *  If outformat is boolean, 'true' is returned if the service is<br>
 * in sync, i.e., a re-deploy would do nothing. If outformat is<br>
 * cli or xml, the changes that the service would do to the<br>
 * network if re-deployed are returned.<br>
 * <br>
 * tailf-info:<br>
 * Check if device config is according to the service<br>
 */
public class CheckSync extends com.cisco.singtel.resourcemanager.base.ActionBase {
    private static final java.lang.String TAG = "check-sync";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "check-sync";
    public static final java.lang.String ACTION_POINT_NAME = "ncsinternal";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent;

    public CheckSync(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        super(null);
        this.parent = parent;
    }

    public CheckSync(se.dataductus.common.nso.path.MaapiPath path) {
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Output request(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Input input) throws com.tailf.conf.ConfException, java.io.IOException {
        com.tailf.conf.ConfXMLParam[] outputParams = channel.requestActionTh(input._toConfXMLParams(), this._getPath().asKeyPath());
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Output output = this.newOutput()._fromConfXMLParams(outputParams);
        return output;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Input newInput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Input(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Output newOutput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Output(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Input parseInput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newInput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.checksync.Output parseOutput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
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

    public CheckSync _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects)null);
        return this;
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
    }
}
