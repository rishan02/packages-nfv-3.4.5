package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects;


/**
 * Description:<br>
 * When a service is committed through the commit-queue, these<br>
 * fields act as references regarding the state of this service<br>
 * instance. In the worst case scenario, a service instance is<br>
 * in the 'failed' state. This means that parts of the queued<br>
 * commit failed.<br>
 *  If a service gets committed through the commit queue and the<br>
 * commit fails, the backpointers in the service data are<br>
 * followed and the affected service instances are updated and<br>
 * set to the 'failed' state.<br>
 *  Depending on the nature of the failure, different techniques<br>
 * to reconciliate the service can be used.<br>
 *  - If all configuration attempts towards a device has failed,<br>
 * it's possible to do a sync-from on the device(s) and then<br>
 * re-deploy all affected services.<br>
 *  - If just a few configuration attempts have failed, the best<br>
 * technique is to fix the error, and then do a sync-to on<br>
 * all devices for the service. (each service has a leaf-list of<br>
 * which devices are touched by the service.<br>
 *  - Yet another technique is to use un-deploy on the service once<br>
 * the error is fixed, and then follow up with a re-deploy.<br>
 *  In the 'failed' state, these fields are reset if a new commit-queue<br>
 * transaction is affecting this service, or the service is re-deployed,<br>
 * or these fields are deleted using the specific purge action.<br>
 * In other cases the 'failed' state will remain.<br>
 */
public class CommitQueue extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "commit-queue";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "commit-queue";
    public static final java.lang.String LEAF_STATUS_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_STATUS_TAG = "status";
    public static final java.lang.String LEAF_QUEUE_ITEM_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_QUEUE_ITEM_TAG = "queue-item";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Status> _status;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList _failedDevice;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.math.BigInteger> _queueItem;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear _clear;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge _purge;

    public CommitQueue(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects parent) {
        super(null);
        this.parent = parent;
    }

    public CommitQueue(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: status</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Status> status() {
        if (this._status == null) {
            this._status = new com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Status>(this, LEAF_STATUS_PREFIX, LEAF_STATUS_TAG, com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Status.CONVERTER);
        }
        return this._status;
    }

    /**
     * Description:<br>
     * If the 'status' leaf is 'failed', this list contains detailed<br>
     * information on the devices that were responsible for the<br>
     * error of the commit queue item.<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList failedDevice() {
        if (this._failedDevice == null) {
            this._failedDevice = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList(this);
        }
        return this._failedDevice;
    }

    /**
     * Description:<br>
     * If the queue item in the commit queue refers to this service<br>
     * this is the queue number.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: queue-item</li>
     * <li> Yang type: uint64</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.math.BigInteger> queueItem() {
        if (this._queueItem == null) {
            this._queueItem = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.math.BigInteger>(this, LEAF_QUEUE_ITEM_PREFIX, LEAF_QUEUE_ITEM_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt64Converter);
        }
        return this._queueItem;
    }

    /**
     * Description:<br>
     * Administratively clear this item.<br>
     * Information about failed devices will remain for this service.<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear clear() {
        if (this._clear == null) {
            this._clear = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear(this);
        }
        return this._clear;
    }

    /**
     * Description:<br>
     * Remove this item.<br>
     * If this action is invoked for an item which is in other states<br>
     * than 'failed' this data migth later reappear since this service<br>
     * is then still affected by an active commit-queue transaction<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge purge() {
        if (this._purge == null) {
            this._purge = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge(this);
        }
        return this._purge;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
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

    public boolean _exists() {
        return this.exists;
    }

    public void _create() {
        this.exists = true;
        this.parent._scheduleWrite(this);
    }

    public void _delete() {
        this.exists = false;
        this.status()._unscheduledDelete();
        this.failedDevice()._detach();
        this.queueItem()._unscheduledDelete();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList child) {
        if (this._failedDevice != child) {
            if (this._failedDevice != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList detached = this._failedDevice;
                this._failedDevice = null;
                detached._detach();
            }

            this._failedDevice = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList detached = this._failedDevice;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.FailedDeviceList)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear child) {
        if (this._clear != child) {
            if (this._clear != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear detached = this._clear;
                this._clear = null;
                detached._detach();
            }

            this._clear = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear detached = this._clear;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Clear)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge child) {
        if (this._purge != child) {
            if (this._purge != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge detached = this._purge;
                this._purge = null;
                detached._detach();
            }

            this._purge = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge detached = this._purge;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.commitqueue.Purge)null);
        return detached;
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

    public CommitQueue _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.Objects)null);
        return this;
    }

    public CommitQueue _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public CommitQueue _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.status()._configureReader(reader);
            this.queueItem()._configureReader(reader);

        }

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.failedDevice()._configureReader(reader.go("failed-device"), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.status()._collectFromReader(reader);
            this.queueItem()._collectFromReader(reader);

        }

        if (scope == com.cisco.singtel.resourcemanager.base.Scope.ALL) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.failedDevice()._collectFromReader(reader.go("failed-device"), scope, mode);
            }
        }
    }

    @Override
    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.DataNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        if (super._scheduleWrite(child) && parent != null) {
            parent._scheduleWrite(this);
            return true;
        }

        return false;
    }

    public CommitQueue _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("commit-queue").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("commit-queue");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.status().exists()) {
            buffer.append(nextIndent).append("status").append(": ").append(this.status().get()).append(java.lang.System.lineSeparator());
        }
        this.failedDevice()._formatTreeTo(buffer, nextIndent, indentStep);
        if (this.queueItem().exists()) {
            buffer.append(nextIndent).append("queueItem").append(": ").append(this.queueItem().get()).append(java.lang.System.lineSeparator());
        }
    }
}
