package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool;


public class Objects extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "objects";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "objects";
    public static final java.lang.String CALL_POINT_NAME = "ncs-rfs-service-hook";
    public static final java.lang.String SERVICE_POINT_NAME = "vdom-apply-profile";
    public static final java.lang.String LEAF_DEVICE_MODIFICATIONS_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEVICE_MODIFICATIONS_TAG = "device-modifications";
    public static final java.lang.String LEAF_DEVICE_LIST_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEVICE_LIST_TAG = "device-list";
    public static final java.lang.String LEAF_USED_BY_CUSTOMER_SERVICE_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_USED_BY_CUSTOMER_SERVICE_TAG = "used-by-customer-service";
    public static final java.lang.String LEAF_OBJECT_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_OBJECT_TAG = "object";
    public static final java.lang.String LEAF_CAPACITY_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_CAPACITY_TAG = "capacity";
    public static final java.lang.String LEAF_CONSUMERS_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_CONSUMERS_TAG = "consumers";
    public static final java.lang.String LEAF_PROFILE_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_PROFILE_TAG = "profile";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList parent;
    private boolean exists = false;
    private ObjectsListKey key;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync _checkSync;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync _deepCheckSync;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy _reDeploy;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy _reactiveReDeploy;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch _touch;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _deviceModifications;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified _modified;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified _directlyModified;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications _getModifications;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> _deviceList;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy _unDeploy;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> _usedByCustomerService;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue _commitQueue;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _object;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Long> _capacity;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> _consumers;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _profile;
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth _bandwidth;

    public Objects(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList parent, ObjectsListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public Objects(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new ObjectsListKey(path.getKey()));
    }

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
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync checkSync() {
        if (this._checkSync == null) {
            this._checkSync = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync(this);
        }
        return this._checkSync;
    }

    /**
     * Description:<br>
     * Check if this service has been undermined on the device itself.<br>
     * The action 'check-sync' compares the output of the service<br>
     * code to what is stored in CDB locally. This action retrieves the<br>
     * configuration from the devices touched by the service and compares<br>
     * the forward diff set of the service to the retrieved data. This<br>
     * is thus a fairly heavy weight operation. As opposed to the check-sync<br>
     * action that invokes the FASTMAP code, this action re-applies the<br>
     * forward diff-set. This is the same output you see when inspecting<br>
     * the 'get-modifications' operational field in the service instance.<br>
     *  If the device is in sync with CDB, the output of this action<br>
     * is identical to the output of the cheaper check-sync action<br>
     * <br>
     * tailf-info:<br>
     * Check if device config is according to the service<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync deepCheckSync() {
        if (this._deepCheckSync == null) {
            this._deepCheckSync = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync(this);
        }
        return this._deepCheckSync;
    }

    /**
     * Description:<br>
     * Run the service code again, possibly writing the changes of<br>
     * the service to the network once again. If the dryrun option is used,<br>
     * the action simply reports (in different formats) what it would do.<br>
     *  Use the option 'reconcile' if the service should reconciliate<br>
     * original data, i.e., take control of that data. This option<br>
     * acknowledges other services controlling the same data. The<br>
     * reference count will indicate how many services control the<br>
     * data.<br>
     * <br>
     * tailf-info:<br>
     * Run/Dryrun the service logic again<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy reDeploy() {
        if (this._reDeploy == null) {
            this._reDeploy = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy(this);
        }
        return this._reDeploy;
    }

    /**
     * Description:<br>
     * This is a tailored re-deploy intended to be used in the reactive<br>
     * FASTMAP scenario. It differs from the ordinary re-deploy in that<br>
     * this action does not take any input parameters.<br>
     *  This action will re-deploy the services as an<br>
     * 'shallow' depth re-deploy. It will be performed as the same<br>
     * user as the original commit. Also, the commit parameters will be<br>
     * identical to the latest commit involving this service.<br>
     * <br>
     * tailf-info:<br>
     * Reactive redeploy of service logic<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy reactiveReDeploy() {
        if (this._reactiveReDeploy == null) {
            this._reactiveReDeploy = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy(this);
        }
        return this._reactiveReDeploy;
    }

    /**
     * Description:<br>
     * This action marks the service as changed.<br>
     *  Executing the action touch followed by a commit is the same as<br>
     * executing the action 're-deploy shallow'.<br>
     *  By using the action 'touch' several re-deploys can be performed<br>
     * in the same transaction.<br>
     * <br>
     * tailf-info:<br>
     * Touch a service<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch touch() {
        if (this._touch == null) {
            this._touch = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch(this);
        }
        return this._touch;
    }

    /**
     * Description:<br>
     * Which device modifications have been performed by this<br>
     * service instance.<br>
     *  This leaf is deprecated. Use the action 'get-modifications'<br>
     * instead.<br>
     * <br>
     * tailf-info:<br>
     * The device configurations this service created<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: device-modifications</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> deviceModifications() {
        if (this._deviceModifications == null) {
            this._deviceModifications = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_DEVICE_MODIFICATIONS_PREFIX, LEAF_DEVICE_MODIFICATIONS_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._deviceModifications;
    }

    /**
     * Description:<br>
     * Devices and other services this service has modified directly or<br>
     * indirectly (through another service).<br>
     * <br>
     * tailf-info:<br>
     * Devices and other services this service modified directly or indirectly.<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified modified() {
        if (this._modified == null) {
            this._modified = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified(this);
        }
        return this._modified;
    }

    /**
     * Description:<br>
     * Devices and other services this service has explicitly<br>
     * modified.<br>
     * <br>
     * tailf-info:<br>
     * Devices and other services this service has explicitly modified.<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified directlyModified() {
        if (this._directlyModified == null) {
            this._directlyModified = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified(this);
        }
        return this._directlyModified;
    }

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
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications getModifications() {
        if (this._getModifications == null) {
            this._getModifications = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications(this);
        }
        return this._getModifications;
    }

    /**
     * Description:<br>
     * A list of managed devices this service instance has manipulated.<br>
     *  This leaf is deprecated. Use 'modified' instead.<br>
     * <br>
     * tailf-info:<br>
     * A list of devices this service instance has manipulated<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: device-list</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> deviceList() {
        if (this._deviceList == null) {
            this._deviceList = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>>(this, LEAF_DEVICE_LIST_PREFIX, LEAF_DEVICE_LIST_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.String>(com.cisco.singtel.resourcemanager.base.Converters.confBufConverter));
        }
        return this._deviceList;
    }

    /**
     * Description:<br>
     * Undo the effects of this service instance but keep the<br>
     * service itself. The service can later be re-deployed. This is<br>
     * a means to deactivate a service, but keep it in the system.<br>
     * <br>
     * tailf-info:<br>
     * Undo the effects of this service<br>
     */
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy unDeploy() {
        if (this._unDeploy == null) {
            this._unDeploy = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy(this);
        }
        return this._unDeploy;
    }

    /**
     * tailf-info:<br>
     * Customer facing services using this service<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: used-by-customer-service</li>
     * <li> Yang type: leafref [/ncs:services/ncs:customer-service/ncs:object-id]</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> usedByCustomerService() {
        if (this._usedByCustomerService == null) {
            this._usedByCustomerService = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>>(this, LEAF_USED_BY_CUSTOMER_SERVICE_PREFIX, LEAF_USED_BY_CUSTOMER_SERVICE_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.String>(com.cisco.singtel.resourcemanager.base.Converters.confBufConverter));
        }
        return this._usedByCustomerService;
    }

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
    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue commitQueue() {
        if (this._commitQueue == null) {
            this._commitQueue = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue(this);
        }
        return this._commitQueue;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: object</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> object() {
        if (this._object == null) {
            this._object = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_OBJECT_PREFIX, LEAF_OBJECT_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._object;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: capacity</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Long> capacity() {
        if (this._capacity == null) {
            this._capacity = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Long>(this, LEAF_CAPACITY_PREFIX, LEAF_CAPACITY_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._capacity;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: consumers</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>> consumers() {
        if (this._consumers == null) {
            this._consumers = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.util.List<java.lang.String>>(this, LEAF_CONSUMERS_PREFIX, LEAF_CONSUMERS_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfListConverter<java.lang.String>(com.cisco.singtel.resourcemanager.base.Converters.confBufConverter));
        }
        return this._consumers;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: profile</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> profile() {
        if (this._profile == null) {
            this._profile = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_PROFILE_PREFIX, LEAF_PROFILE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._profile;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth bandwidth() {
        if (this._bandwidth == null) {
            this._bandwidth = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth(this);
        }
        return this._bandwidth;
    }

    public ObjectsListKey _getKey() {
        return this.key;
    }

    protected void _setKey(ObjectsListKey key) {
        this.object()._unscheduledSet(key.object());

        this.key = key;
    }
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
            super._setPath(parent._getPath().elem(this._getKey().asConfKey()));
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
        this.deviceModifications()._unscheduledDelete();
        this.modified()._detach();
        this.directlyModified()._detach();
        this.deviceList()._unscheduledDelete();
        this.usedByCustomerService()._unscheduledDelete();
        this.commitQueue()._detach();
        this.object()._unscheduledDelete();
        this.capacity()._unscheduledDelete();
        this.consumers()._unscheduledDelete();
        this.profile()._unscheduledDelete();
        this.bandwidth()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync child) {
        if (this._checkSync != child) {
            if (this._checkSync != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync detached = this._checkSync;
                this._checkSync = null;
                detached._detach();
            }

            this._checkSync = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync detached = this._checkSync;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CheckSync)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync child) {
        if (this._deepCheckSync != child) {
            if (this._deepCheckSync != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync detached = this._deepCheckSync;
                this._deepCheckSync = null;
                detached._detach();
            }

            this._deepCheckSync = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync detached = this._deepCheckSync;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy child) {
        if (this._reDeploy != child) {
            if (this._reDeploy != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy detached = this._reDeploy;
                this._reDeploy = null;
                detached._detach();
            }

            this._reDeploy = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy detached = this._reDeploy;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReDeploy)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy child) {
        if (this._reactiveReDeploy != child) {
            if (this._reactiveReDeploy != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy detached = this._reactiveReDeploy;
                this._reactiveReDeploy = null;
                detached._detach();
            }

            this._reactiveReDeploy = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy detached = this._reactiveReDeploy;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.ReactiveReDeploy)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch child) {
        if (this._touch != child) {
            if (this._touch != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch detached = this._touch;
                this._touch = null;
                detached._detach();
            }

            this._touch = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch detached = this._touch;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Touch)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified child) {
        if (this._modified != child) {
            if (this._modified != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified detached = this._modified;
                this._modified = null;
                detached._detach();
            }

            this._modified = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified detached = this._modified;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Modified)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified child) {
        if (this._directlyModified != child) {
            if (this._directlyModified != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified detached = this._directlyModified;
                this._directlyModified = null;
                detached._detach();
            }

            this._directlyModified = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified detached = this._directlyModified;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DirectlyModified)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications child) {
        if (this._getModifications != child) {
            if (this._getModifications != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications detached = this._getModifications;
                this._getModifications = null;
                detached._detach();
            }

            this._getModifications = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications detached = this._getModifications;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy child) {
        if (this._unDeploy != child) {
            if (this._unDeploy != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy detached = this._unDeploy;
                this._unDeploy = null;
                detached._detach();
            }

            this._unDeploy = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy detached = this._unDeploy;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.UnDeploy)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue child) {
        if (this._commitQueue != child) {
            if (this._commitQueue != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue detached = this._commitQueue;
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

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue detached = this._commitQueue;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.CommitQueue)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth child) {
        if (this._bandwidth != child) {
            if (this._bandwidth != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth detached = this._bandwidth;
                this._bandwidth = null;
                detached._detach();
            }

            this._bandwidth = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth _detach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth child) {
        com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth detached = this._bandwidth;
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.Bandwidth)null);
        return detached;
    }

    public com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList _attach(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList detached = this.parent;
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

    public Objects _detach() {
        this._attach((com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.ObjectsList)null);
        return this;
    }

    public Objects _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Objects _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            this.capacity()._configureReader(reader);
            this.consumers()._configureReader(reader);
            this.profile()._configureReader(reader);

        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.deviceModifications()._configureReader(reader);
            this.deviceList()._configureReader(reader);
            this.usedByCustomerService()._configureReader(reader);

        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {

            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.modified()._configureReader(reader.go("modified"), channel, scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.directlyModified()._configureReader(reader.go("directly-modified"), channel, scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.commitQueue()._configureReader(reader.go("commit-queue"), channel, scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.bandwidth()._configureReader(reader.go("bandwidth"), channel, scope, mode);
            }
        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
            this.capacity()._collectFromReader(reader);
            this.consumers()._collectFromReader(reader);
            this.profile()._collectFromReader(reader);

        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.deviceModifications()._collectFromReader(reader);
            this.deviceList()._collectFromReader(reader);
            this.usedByCustomerService()._collectFromReader(reader);

        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.modified()._collectFromReader(reader.go("modified"), scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.directlyModified()._collectFromReader(reader.go("directly-modified"), scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                this.commitQueue()._collectFromReader(reader.go("commit-queue"), scope, mode);
            }
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.CONFIG)) {
                this.bandwidth()._collectFromReader(reader.go("bandwidth"), scope, mode);
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

    public Objects _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.elem(this._getKey().asConfKey()).setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.elem(this._getKey().asConfKey());
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.deviceModifications().exists()) {
            buffer.append(nextIndent).append("deviceModifications").append(": ").append(this.deviceModifications().get()).append(java.lang.System.lineSeparator());
        }
        if (this.modified()._exists()) {
            this.modified()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.directlyModified()._exists()) {
            this.directlyModified()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        buffer.append(nextIndent).append("deviceList").append(":");
        if (this.deviceList().exists()) {
            for (java.lang.Object elem : this.deviceList().get()) {
                buffer.append(java.lang.System.lineSeparator()).append(nextIndent + indentStep).append(elem);
            }
        } else {
            buffer.append(" null");
        }

        buffer.append(java.lang.System.lineSeparator());
        buffer.append(nextIndent).append("usedByCustomerService").append(":");
        if (this.usedByCustomerService().exists()) {
            for (java.lang.Object elem : this.usedByCustomerService().get()) {
                buffer.append(java.lang.System.lineSeparator()).append(nextIndent + indentStep).append(elem);
            }
        } else {
            buffer.append(" null");
        }

        buffer.append(java.lang.System.lineSeparator());
        if (this.commitQueue()._exists()) {
            this.commitQueue()._formatTreeTo(buffer, nextIndent, indentStep);
        }
        if (this.object().exists()) {
            buffer.append(nextIndent).append("object").append(": ").append(this.object().get()).append(java.lang.System.lineSeparator());
        }
        if (this.capacity().exists()) {
            buffer.append(nextIndent).append("capacity").append(": ").append(this.capacity().get()).append(java.lang.System.lineSeparator());
        }
        buffer.append(nextIndent).append("consumers").append(":");
        if (this.consumers().exists()) {
            for (java.lang.Object elem : this.consumers().get()) {
                buffer.append(java.lang.System.lineSeparator()).append(nextIndent + indentStep).append(elem);
            }
        } else {
            buffer.append(" null");
        }

        buffer.append(java.lang.System.lineSeparator());
        if (this.profile().exists()) {
            buffer.append(nextIndent).append("profile").append(": ").append(this.profile().get()).append(java.lang.System.lineSeparator());
        }
        if (this.bandwidth()._exists()) {
            this.bandwidth()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
