package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.commitqueue;


/**
 * Description:<br>
 * Commit the data synchronously to the commit-queue.<br>
 *  The operation does not return until the transaction has been<br>
 * sent to all devices, or a timeout occurs.<br>
 *  If no device is involved in the transaction, the operation<br>
 * returns 'ok' directly.<br>
 * <br>
 * Presence:<br>
 * Commit through the commit-queue and wait for completion<br>
 */
public class Sync extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "sync";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "sync";
    public static final java.lang.String LEAF_TIME_OUT_CHOICE_TIMEOUT_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_TIME_OUT_CHOICE_TIMEOUT_TAG = "timeout";
    public static final java.lang.String LEAF_TIME_OUT_CHOICE_INFINITY_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_TIME_OUT_CHOICE_INFINITY_TAG = "infinity";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> _timeout;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _infinity;
    private java.lang.String _choice_timeOutChoice_case;

    public Sync(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue parent) {
        super(null);
        this.parent = parent;
    }

    public Sync(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Description:<br>
     * Specifies a maximum number of seconds to wait for the<br>
     * transaction to be committed. If the timer expires, the<br>
     * transaction is kept in the commit-queue, and the operation<br>
     * returns successfully. The leaf 'commit-queue/status'<br>
     * is set to 'timeout', and the leaf 'commit-queue/id' is<br>
     * set to the queue identifier in the result.<br>
     *  If the timeout is not set, the operation waits until the<br>
     * transaction is committed.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: timeout</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long> timeout() {
        if (this._timeout == null) {
            this._timeout = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Long>(this, LEAF_TIME_OUT_CHOICE_TIMEOUT_PREFIX, LEAF_TIME_OUT_CHOICE_TIMEOUT_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._timeout;
    }

    /**
     * Description:<br>
     * Wait infinitely for the lock, this is the default<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: infinity</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> infinity() {
        if (this._infinity == null) {
            this._infinity = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_TIME_OUT_CHOICE_INFINITY_PREFIX, LEAF_TIME_OUT_CHOICE_INFINITY_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._infinity;
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
        this.timeout()._unscheduledDelete();
        this.infinity()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue detached = this.parent;
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

    public Sync _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue)null);
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
                case "singtel-rm:timeout": {
                    this.timeout()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:infinity": {
                    this.infinity()._unscheduledSet(param.getValue());
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
        if (this.timeout()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.timeout()._getPrefix(), this.timeout()._getTag(), this.timeout().toConfValue()));
        }
        if (this.infinity()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.infinity()._getPrefix(), this.infinity()._getTag(), this.infinity().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.timeout().exists()) {
            buffer.append(nextIndent).append("timeout").append(": ").append(this.timeout().get()).append(java.lang.System.lineSeparator());
        }
        if (this.infinity().exists()) {
            buffer.append(nextIndent).append("infinity").append(": ").append(this.infinity().get()).append(java.lang.System.lineSeparator());
        }
    }
}
