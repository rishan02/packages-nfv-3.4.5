package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input;


/**
 * Presence:<br>
 * Commit through the commit-queue<br>
 */
public class CommitQueue extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "commit-queue";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "commit-queue";
    public static final java.lang.String LEAF_TAG_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_TAG_TAG = "tag";
    public static final java.lang.String LEAF_OPERATION_MODE_ASYNC_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_OPERATION_MODE_ASYNC_TAG = "async";
    public static final java.lang.String LEAF_BLOCK_OTHERS_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_BLOCK_OTHERS_TAG = "block-others";
    public static final java.lang.String LEAF_LOCK_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_LOCK_TAG = "lock";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _tag;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _async;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync _sync;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _blockOthers;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _lock;
    private java.lang.String _choice_operationMode_case;

    public CommitQueue(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input parent) {
        super(null);
        this.parent = parent;
    }

    public CommitQueue(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Description:<br>
     * User defined opaque tag.<br>
     * The tag is present in all notifications and events<br>
     * sent referencing the specific queue item.<br>
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
     * Description:<br>
     * This flags means that if some device is non-operational or<br>
     * has data waiting in the commit-queue, the data in this<br>
     * transaction will be placed in the commit-queue.<br>
     *  The operation returns successfully if the transaction has<br>
     * been successfully placed in the queue. The leaf<br>
     * 'commit-queue/id' is set to the queue identifier and the<br>
     * leaf 'commit-queue/status' is set to 'async' in the result.<br>
     *  Note that this flag has no effect if<br>
     * /devices/commit-queue/enabled-by-default is 'true', since all<br>
     * commits go through the queue in this case.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: async</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> async() {
        if (this._async == null) {
            this._async = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_OPERATION_MODE_ASYNC_PREFIX, LEAF_OPERATION_MODE_ASYNC_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._async;
    }

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
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync sync() {
        if (this._sync == null) {
            this._sync = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync(this);
        }
        return this._sync;
    }

    /**
     * Description:<br>
     * The resulting queue item will block subsequent queue items,<br>
     * which use any of the devices in this queue item, from<br>
     * being queued.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: block-others</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> blockOthers() {
        if (this._blockOthers == null) {
            this._blockOthers = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_BLOCK_OTHERS_PREFIX, LEAF_BLOCK_OTHERS_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._blockOthers;
    }

    /**
     * Description:<br>
     * Place a lock on the resulting queue item. The queue<br>
     * item will not be processed until it has been unlocked,<br>
     * see the actions 'unlock' and 'lock' in<br>
     * /devices/commit-queue/queue-item'.<br>
     *  No following queue items, using the same devices, will be<br>
     * allowed to execute as long as the lock is in place.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: lock</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> lock() {
        if (this._lock == null) {
            this._lock = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_LOCK_PREFIX, LEAF_LOCK_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._lock;
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
        this.tag()._unscheduledDelete();
        this.async()._unscheduledDelete();
        this.sync()._detach();
        this.blockOthers()._unscheduledDelete();
        this.lock()._unscheduledDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync child) {
        if (this._sync != child) {
            if (this._sync != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync detached = this._sync;
                this._sync = null;
                detached._detach();
            }

            this._sync = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync detached = this._sync;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.input.commitqueue.Sync)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input detached = this.parent;
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
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.undeploy.Input)null);
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
                case "singtel-rm:tag": {
                    this.tag()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:async": {
                    this.async()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:sync": {
                    i = this.sync()._parseConfXMLParams(params, i + 1, end);
                    break;
                }
                case "singtel-rm:block-others": {
                    this.blockOthers()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:lock": {
                    this.lock()._unscheduledSet(param.getValue());
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
        if (this.tag()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.tag()._getPrefix(), this.tag()._getTag(), this.tag().toConfValue()));
        }
        if (this.async()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.async()._getPrefix(), this.async()._getTag(), this.async().toConfValue()));
        }
        if (this.sync()._exists()) {
            this.sync()._writeConfXMLParams(params);
        }
        if (this.blockOthers()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.blockOthers()._getPrefix(), this.blockOthers()._getTag(), this.blockOthers().toConfValue()));
        }
        if (this.lock()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.lock()._getPrefix(), this.lock()._getTag(), this.lock().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.tag().exists()) {
            buffer.append(nextIndent).append("tag").append(": ").append(this.tag().get()).append(java.lang.System.lineSeparator());
        }
        if (this.async().exists()) {
            buffer.append(nextIndent).append("async").append(": ").append(this.async().get()).append(java.lang.System.lineSeparator());
        }
        if (this.sync()._exists()) {
            this.sync()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.blockOthers().exists()) {
            buffer.append(nextIndent).append("blockOthers").append(": ").append(this.blockOthers().get()).append(java.lang.System.lineSeparator());
        }
        if (this.lock().exists()) {
            buffer.append(nextIndent).append("lock").append(": ").append(this.lock().get()).append(java.lang.System.lineSeparator());
        }
    }
}
