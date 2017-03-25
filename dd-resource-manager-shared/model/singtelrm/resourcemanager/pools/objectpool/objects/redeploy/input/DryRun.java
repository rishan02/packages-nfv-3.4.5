package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input;


/**
 */
public class DryRun extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "dry-run";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "dry-run";
    public static final java.lang.String LEAF_OUTFORMAT_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_OUTFORMAT_TAG = "outformat";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.Outformat3> _outformat;

    public DryRun(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input parent) {
        super(null);
        this.parent = parent;
    }

    public DryRun(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: outformat</li>
     * <li> Yang type: outformat3</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.Outformat3> outformat() {
        if (this._outformat == null) {
            this._outformat = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.Outformat3>(this, LEAF_OUTFORMAT_PREFIX, LEAF_OUTFORMAT_TAG, com.cisco.singtel.resourcemanager.model.ncs.Outformat3.CONVERTER);
        }
        return this._outformat;
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
        this.outformat()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input detached = this.parent;
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

    public DryRun _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input)null);
        return this;
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
            if (parent != null && !parent._exists()) {
                parent._scheduleWrite(this);
            }
        }

        return false;
    }

    public int _parseConfXMLParams(com.tailf.conf.ConfXMLParam[] params, int start, int end) throws com.tailf.conf.ConfException {
        int i;
        for (i = start; i < end; i++) {
            com.tailf.conf.ConfXMLParam param = params[i];
            if (param instanceof com.tailf.conf.ConfXMLParamStop) {
                break;
            }

            java.lang.String prefix = null;
            com.tailf.conf.ConfNamespace ns = param.getConfNamespace();
            if (ns != null) {
                prefix = ns.prefix();
            }
            java.lang.String label = java.lang.String.format("%s:%s", prefix != null ? prefix : PREFIX, param.getTag());

            switch (label) {
                case "singtel-rm:outformat": {
                    this.outformat()._unscheduledSet(param.getValue());
                    break;
                }
                default: {
                    throw new com.tailf.conf.ConfException("Unknown element: " + param.getTag());
                }
            }
        }


        this.exists = true;

        return i;
    }

    public void _writeConfXMLParams(java.util.List<com.tailf.conf.ConfXMLParam> params) throws com.tailf.conf.ConfException {
        params.add(new com.tailf.conf.ConfXMLParamStart(this._getPrefix(), this._getTag()));
        if (this.outformat()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.outformat()._getPrefix(), this.outformat()._getTag(), this.outformat().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.outformat().exists()) {
            buffer.append(nextIndent).append("outformat").append(": ").append(this.outformat().get()).append(java.lang.System.lineSeparator());
        }
    }
}
