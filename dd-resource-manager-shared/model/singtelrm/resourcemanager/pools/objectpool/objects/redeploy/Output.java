package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy;


public class Output extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "output";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "output";
    public static final java.lang.String LEAF_OUTFORMAT_CLI_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_OUTFORMAT_CLI_TAG = "cli";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _cli;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native __native;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue _commitQueue;
    private java.lang.String _choice_outformat_case;

    public Output(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy parent) {
        super(null);
    }

    public Output(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: cli</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> cli() {
        if (this._cli == null) {
            this._cli = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_OUTFORMAT_CLI_PREFIX, LEAF_OUTFORMAT_CLI_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._cli;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native _native() {
        if (this.__native == null) {
            this.__native = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native(this);
        }
        return this.__native;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue commitQueue() {
        if (this._commitQueue == null) {
            this._commitQueue = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue(this);
        }
        return this._commitQueue;
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
    }

    public void _delete() {
        this.exists = false;
        this.cli()._unscheduledDelete();
        this._native()._detach();
        this.commitQueue()._detach();
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native child) {
        if (this.__native != child) {
            if (this.__native != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native detached = this.__native;
                this.__native = null;
                detached._detach();
            }

            this.__native = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native detached = this.__native;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.Native)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue child) {
        if (this._commitQueue != child) {
            if (this._commitQueue != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue detached = this._commitQueue;
                this._commitQueue = null;
                detached._detach();
            }

            this._commitQueue = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue detached = this._commitQueue;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.CommitQueue)null);
        return detached;
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        return false;
    }

    public Output _fromConfXMLParams(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
        this._parseConfXMLParams(params, 0, params.length);
        return this;
    }

    public com.tailf.conf.ConfXMLParam[] _toConfXMLParams() throws com.tailf.conf.ConfException {
        java.util.List<com.tailf.conf.ConfXMLParam> params = new java.util.ArrayList<>();
        this._writeConfXMLParams(params);
        return params.toArray(new com.tailf.conf.ConfXMLParam[params.size()]);
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
                case "singtel-rm:cli": {
                    this.cli()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:native": {
                    i = this._native()._parseConfXMLParams(params, i + 1, end);
                    break;
                }
                case "singtel-rm:commit-queue": {
                    i = this.commitQueue()._parseConfXMLParams(params, i + 1, end);
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
        if (this.cli()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.cli()._getPrefix(), this.cli()._getTag(), this.cli().toConfValue()));
        }
        if (this._native()._exists()) {
            this._native()._writeConfXMLParams(params);
        }
        if (this.commitQueue()._exists()) {
            this.commitQueue()._writeConfXMLParams(params);
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.cli().exists()) {
            buffer.append(nextIndent).append("cli").append(": ").append(this.cli().get()).append(java.lang.System.lineSeparator());
        }
        if (this._native()._exists()) {
            this._native()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.commitQueue()._exists()) {
            this.commitQueue()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
