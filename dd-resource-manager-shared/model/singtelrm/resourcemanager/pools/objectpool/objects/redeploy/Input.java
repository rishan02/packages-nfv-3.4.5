package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy;


public class Input extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "input";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "input";
    public static final java.lang.String LEAF_NO_REVISION_DROP_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NO_REVISION_DROP_TAG = "no-revision-drop";
    public static final java.lang.String LEAF_NO_OVERWRITE_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NO_OVERWRITE_TAG = "no-overwrite";
    public static final java.lang.String LEAF_NO_NETWORKING_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NO_NETWORKING_TAG = "no-networking";
    public static final java.lang.String LEAF_NO_OUT_OF_SYNC_CHECK_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_NO_OUT_OF_SYNC_CHECK_TAG = "no-out-of-sync-check";
    public static final java.lang.String LEAF_DEPTH_DEEP_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEPTH_DEEP_TAG = "deep";
    public static final java.lang.String LEAF_DEPTH_SHALLOW_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEPTH_SHALLOW_TAG = "shallow";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun _dryRun;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _noRevisionDrop;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _noOverwrite;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _noNetworking;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _noOutOfSyncCheck;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue _commitQueue;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _deep;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _shallow;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile _reconcile;
    private java.lang.String _choice_depth_case;

    public Input(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy parent) {
        super(null);
    }

    public Input(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun dryRun() {
        if (this._dryRun == null) {
            this._dryRun = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun(this);
        }
        return this._dryRun;
    }

    /**
     * Description:<br>
     * This flags means that NCS will not run its data model revision<br>
     * algorithm, which requires all participating managed devices<br>
     * to have all parts of the data models for all data contained<br>
     * in this transaction. Thus, this flag forces NCS to never<br>
     * silently drop any data set operations towards a device.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: no-revision-drop</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> noRevisionDrop() {
        if (this._noRevisionDrop == null) {
            this._noRevisionDrop = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_NO_REVISION_DROP_PREFIX, LEAF_NO_REVISION_DROP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._noRevisionDrop;
    }

    /**
     * Description:<br>
     * This flags means that NCS will check that the data that<br>
     * should be modified has not changed on the device compared<br>
     * to NCS's view of the data. This is fine-granular sync check;<br>
     * NCS verifies that NCS and the device is in sync regarding<br>
     * the data that will be modified. If they are not in sync,<br>
     * the transaction is aborted.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: no-overwrite</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> noOverwrite() {
        if (this._noOverwrite == null) {
            this._noOverwrite = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_NO_OVERWRITE_PREFIX, LEAF_NO_OVERWRITE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._noOverwrite;
    }

    /**
     * Description:<br>
     * Do not send any data to the devices. Even if the transaction<br>
     * manipulates data below /devices/device/config, nothing will<br>
     * be sent to the managed devices. This is a way to manipulate<br>
     * CDB in NCS without generating any southbound traffic.<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: no-networking</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> noNetworking() {
        if (this._noNetworking == null) {
            this._noNetworking = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_NO_NETWORKING_PREFIX, LEAF_NO_NETWORKING_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._noNetworking;
    }

    /**
     * Description:<br>
     * Continue with the transaction even if NCS detects that a device's<br>
     * configuration is out of sync. The device's sync state is assumed<br>
     * to be unknown after such commit and the stored transaction id<br>
     * value is cleared<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: no-out-of-sync-check</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> noOutOfSyncCheck() {
        if (this._noOutOfSyncCheck == null) {
            this._noOutOfSyncCheck = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_NO_OUT_OF_SYNC_CHECK_PREFIX, LEAF_NO_OUT_OF_SYNC_CHECK_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._noOutOfSyncCheck;
    }

    /**
     * Presence:<br>
     * Commit through the commit-queue<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue commitQueue() {
        if (this._commitQueue == null) {
            this._commitQueue = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue(this);
        }
        return this._commitQueue;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: deep</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> deep() {
        if (this._deep == null) {
            this._deep = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_DEPTH_DEEP_PREFIX, LEAF_DEPTH_DEEP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._deep;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: shallow</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> shallow() {
        if (this._shallow == null) {
            this._shallow = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_DEPTH_SHALLOW_PREFIX, LEAF_DEPTH_SHALLOW_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._shallow;
    }

    /**
     * Description:<br>
     * Reconcile the service data. All data which existed before<br>
     * the service was created will now be owned by the service.<br>
     * When the service is removed that data will also be removed.<br>
     * In technical terms the reference count will be decreased<br>
     * by one for everything which existed prior to the service.<br>
     *  If manually configured data exists below in the configuration<br>
     * tree that data is kept unless the option<br>
     * 'discard-non-service-config' is used.<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile reconcile() {
        if (this._reconcile == null) {
            this._reconcile = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile(this);
        }
        return this._reconcile;
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
        this.dryRun()._detach();
        this.noRevisionDrop()._unscheduledDelete();
        this.noOverwrite()._unscheduledDelete();
        this.noNetworking()._unscheduledDelete();
        this.noOutOfSyncCheck()._unscheduledDelete();
        this.commitQueue()._detach();
        this.deep()._unscheduledDelete();
        this.shallow()._unscheduledDelete();
        this.reconcile()._detach();
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun child) {
        if (this._dryRun != child) {
            if (this._dryRun != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun detached = this._dryRun;
                this._dryRun = null;
                detached._detach();
            }

            this._dryRun = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun detached = this._dryRun;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.DryRun)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue child) {
        if (this._commitQueue != child) {
            if (this._commitQueue != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue detached = this._commitQueue;
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue detached = this._commitQueue;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.CommitQueue)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile child) {
        if (this._reconcile != child) {
            if (this._reconcile != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile detached = this._reconcile;
                this._reconcile = null;
                detached._detach();
            }

            this._reconcile = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile detached = this._reconcile;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input.Reconcile)null);
        return detached;
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        return false;
    }

    public Input _fromConfXMLParams(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
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
                case "singtel-rm:dry-run": {
                    i = this.dryRun()._parseConfXMLParams(params, i + 1, end);
                    break;
                }
                case "singtel-rm:no-revision-drop": {
                    this.noRevisionDrop()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:no-overwrite": {
                    this.noOverwrite()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:no-networking": {
                    this.noNetworking()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:no-out-of-sync-check": {
                    this.noOutOfSyncCheck()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:commit-queue": {
                    i = this.commitQueue()._parseConfXMLParams(params, i + 1, end);
                    break;
                }
                case "singtel-rm:deep": {
                    this.deep()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:shallow": {
                    this.shallow()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:reconcile": {
                    i = this.reconcile()._parseConfXMLParams(params, i + 1, end);
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
        if (this.dryRun()._exists()) {
            this.dryRun()._writeConfXMLParams(params);
        }
        if (this.noRevisionDrop()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.noRevisionDrop()._getPrefix(), this.noRevisionDrop()._getTag(), this.noRevisionDrop().toConfValue()));
        }
        if (this.noOverwrite()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.noOverwrite()._getPrefix(), this.noOverwrite()._getTag(), this.noOverwrite().toConfValue()));
        }
        if (this.noNetworking()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.noNetworking()._getPrefix(), this.noNetworking()._getTag(), this.noNetworking().toConfValue()));
        }
        if (this.noOutOfSyncCheck()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.noOutOfSyncCheck()._getPrefix(), this.noOutOfSyncCheck()._getTag(), this.noOutOfSyncCheck().toConfValue()));
        }
        if (this.commitQueue()._exists()) {
            this.commitQueue()._writeConfXMLParams(params);
        }
        if (this.deep()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.deep()._getPrefix(), this.deep()._getTag(), this.deep().toConfValue()));
        }
        if (this.shallow()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.shallow()._getPrefix(), this.shallow()._getTag(), this.shallow().toConfValue()));
        }
        if (this.reconcile()._exists()) {
            this.reconcile()._writeConfXMLParams(params);
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.dryRun()._exists()) {
            this.dryRun()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.noRevisionDrop().exists()) {
            buffer.append(nextIndent).append("noRevisionDrop").append(": ").append(this.noRevisionDrop().get()).append(java.lang.System.lineSeparator());
        }
        if (this.noOverwrite().exists()) {
            buffer.append(nextIndent).append("noOverwrite").append(": ").append(this.noOverwrite().get()).append(java.lang.System.lineSeparator());
        }
        if (this.noNetworking().exists()) {
            buffer.append(nextIndent).append("noNetworking").append(": ").append(this.noNetworking().get()).append(java.lang.System.lineSeparator());
        }
        if (this.noOutOfSyncCheck().exists()) {
            buffer.append(nextIndent).append("noOutOfSyncCheck").append(": ").append(this.noOutOfSyncCheck().get()).append(java.lang.System.lineSeparator());
        }
        if (this.commitQueue()._exists()) {
            this.commitQueue()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.deep().exists()) {
            buffer.append(nextIndent).append("deep").append(": ").append(this.deep().get()).append(java.lang.System.lineSeparator());
        }
        if (this.shallow().exists()) {
            buffer.append(nextIndent).append("shallow").append(": ").append(this.shallow().get()).append(java.lang.System.lineSeparator());
        }
        if (this.reconcile()._exists()) {
            this.reconcile()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
