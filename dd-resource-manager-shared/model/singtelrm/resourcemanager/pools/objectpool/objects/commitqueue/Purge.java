package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue;

/**
 * Description:<br>
 * Remove this item.<br>
 * If this action is invoked for an item which is in other states<br>
 * than 'failed' this data migth later reappear since this service<br>
 * is then still affected by an active commit-queue transaction<br>
 */
public class Purge extends com.cisco.singtel.resourcemanager.base.ActionBase {
    private static final java.lang.String TAG = "purge";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "purge";
    public static final java.lang.String ACTION_POINT_NAME = "ncsinternal";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue parent;

    public Purge(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue parent) {
        super(null);
        this.parent = parent;
    }

    public Purge(se.dataductus.common.nso.path.MaapiPath path) {
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Output request(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Input input) throws com.tailf.conf.ConfException, java.io.IOException {
        com.tailf.conf.ConfXMLParam[] outputParams = channel.requestActionTh(input._toConfXMLParams(), this._getPath().asKeyPath());
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Output output = this.newOutput()._fromConfXMLParams(outputParams);
        return output;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Input newInput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Input(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Output newOutput() {
        return new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Output(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Input parseInput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newInput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.purge.Output parseOutput(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        return this.newOutput()._fromConfXMLParams(params);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue detached = this.parent;
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

    public Purge _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue)null);
        return this;
    }

    @Override
    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
    }
}
