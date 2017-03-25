package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.output;


public class CommitQueue extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "commit-queue";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "commit-queue";
    public static final java.lang.String LEAF_ID_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_ID_TAG = "id";
    public static final java.lang.String LEAF_TAG_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_TAG_TAG = "tag";
    public static final java.lang.String LEAF_STATUS_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_STATUS_TAG = "status";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.math.BigInteger> _id;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _tag;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.output.commitqueue.Status> _status;

    public CommitQueue(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output parent) {
        super(null);
        this.parent = parent;
    }

    public CommitQueue(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Description:<br>
     * This leaf is returned if 'commit-queue/async' was given in the<br>
     * input parameters, or if 'commit-queue/sync' and 'timeout' was<br>
     * given, and the transaction has been queued.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: id</li>
     * <li> Yang type: leafref [/ncs:devices/ncs:commit-queue/ncs:queue-item/ncs:id]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.math.BigInteger> id() {
        if (this._id == null) {
            this._id = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.math.BigInteger>(this, LEAF_ID_PREFIX, LEAF_ID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt64Converter);
        }
        return this._id;
    }

    /**
     * Description:<br>
     * This leaf is returned if 'commit-queue/tag' was specified in the<br>
     * input parameters<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: tag</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> tag() {
        if (this._tag == null) {
            this._tag = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_TAG_PREFIX, LEAF_TAG_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._tag;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: status</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.output.commitqueue.Status> status() {
        if (this._status == null) {
            this._status = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.output.commitqueue.Status>(this, LEAF_STATUS_PREFIX, LEAF_STATUS_TAG, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.output.commitqueue.Status.CONVERTER);
        }
        return this._status;
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
        this.id()._unscheduledDelete();
        this.tag()._unscheduledDelete();
        this.status()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output detached = this.parent;
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

    public CommitQueue _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Output)null);
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
                case "singtel-rm:id": {
                    this.id()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:tag": {
                    this.tag()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:status": {
                    this.status()._unscheduledSet(param.getValue());
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
        if (this.id()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.id()._getPrefix(), this.id()._getTag(), this.id().toConfValue()));
        }
        if (this.tag()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.tag()._getPrefix(), this.tag()._getTag(), this.tag().toConfValue()));
        }
        if (this.status()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.status()._getPrefix(), this.status()._getTag(), this.status().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.id().exists()) {
            buffer.append(nextIndent).append("id").append(": ").append(this.id().get()).append(java.lang.System.lineSeparator());
        }
        if (this.tag().exists()) {
            buffer.append(nextIndent).append("tag").append(": ").append(this.tag().get()).append(java.lang.System.lineSeparator());
        }
        if (this.status().exists()) {
            buffer.append(nextIndent).append("status").append(": ").append(this.status().get()).append(java.lang.System.lineSeparator());
        }
    }
}
