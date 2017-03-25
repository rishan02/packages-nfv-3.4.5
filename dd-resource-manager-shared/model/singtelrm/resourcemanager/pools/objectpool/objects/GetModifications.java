package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects;

/**
 * Description:<br>
 * Returns the data this service modified, either<br>
 * in CLI diff format, or XML edit-config format. This<br>
 * data is only available if the parameter<br>
 * '/services/global-settings/collect-forward-diff' is set to true.<br>
 *  If the parameter 'reverse' is given the modifications needed<br>
 * to 'reverse' the effect of the service is shown. This will<br>
 * be applied if the service is deleted. This data is always<br>
 * available.<br>
 *  The parameter deep/shallow controls if the modifications shown<br>
 * are for this service only or for all modiefied services as well.<br>
 * <br>
 * tailf-info:<br>
 * Get the data this service created<br>
 */
public class GetModifications extends com.cisco.singtel.resourcemanager.base.ActionBase {
    private static final java.lang.String TAG = "get-modifications";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "get-modifications";
    public static final java.lang.String ACTION_POINT_NAME = "ncsinternal";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent;

    public GetModifications(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        super(null);
        this.parent = parent;
    }

    public GetModifications(se.dataductus.common.nso.path.MaapiPath path) {
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Output request(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Input input) throws com.tailf.conf.ConfException, java.io.IOException {
        com.tailf.conf.ConfXMLParam[] outputParams = channel.requestActionTh(input._toConfXMLParams(), this._getPath().asKeyPath());
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Output output = this.newOutput()._fromConfXMLParams(outputParams);
        return output;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Input newInput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Input(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Output newOutput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Output(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Input parseInput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newInput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications.Output parseOutput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
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

    public GetModifications _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects)null);
        return this;
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
    }
}
