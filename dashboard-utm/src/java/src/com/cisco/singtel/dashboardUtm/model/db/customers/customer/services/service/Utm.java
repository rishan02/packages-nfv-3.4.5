package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service;


/**
 * Presence:<br>
 * true<br>
 */
public class Utm extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "utm";
    private static final java.lang.String PREFIX = "dbutm";
    private static final java.lang.String LABEL = "dbutm:utm";
    public static final java.lang.String LEAF_SERVICE_TYPE_PREFIX = "dbutm";
    public static final java.lang.String LEAF_SERVICE_TYPE_TAG = "service-type";
    public static final java.lang.String LEAF_DEPLOYMENT_TYPE_PREFIX = "dbutm";
    public static final java.lang.String LEAF_DEPLOYMENT_TYPE_TAG = "deployment-type";
    public static final java.lang.String LEAF_HOSTNAME_PREFIX = "dbutm";
    public static final java.lang.String LEAF_HOSTNAME_TAG = "hostname";
    public static final java.lang.String LEAF_LOCAL_VDOM_ID_PREFIX = "dbutm";
    public static final java.lang.String LEAF_LOCAL_VDOM_ID_TAG = "local-vdom-id";
    public static final java.lang.String LEAF_PROFILE_PREFIX = "dbutm";
    public static final java.lang.String LEAF_PROFILE_TAG = "profile";
    private com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.ServiceType> _serviceType;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.DeploymentType> _deploymentType;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _hostname;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _localVdomId;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _profile;

    public Utm(com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service parent) {
        super(null);
        this.parent = parent;
    }

    public Utm(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: service-type</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.ServiceType> serviceType() {
        if (this._serviceType == null) {
            this._serviceType = new com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.ServiceType>(this, LEAF_SERVICE_TYPE_PREFIX, LEAF_SERVICE_TYPE_TAG, com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.ServiceType.CONVERTER);
        }
        return this._serviceType;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: deployment-type</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.DeploymentType> deploymentType() {
        if (this._deploymentType == null) {
            this._deploymentType = new com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.DeploymentType>(this, LEAF_DEPLOYMENT_TYPE_PREFIX, LEAF_DEPLOYMENT_TYPE_TAG, com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm.DeploymentType.CONVERTER);
        }
        return this._deploymentType;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: hostname</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> hostname() {
        if (this._hostname == null) {
            this._hostname = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_HOSTNAME_PREFIX, LEAF_HOSTNAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._hostname;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: local-vdom-id</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> localVdomId() {
        if (this._localVdomId == null) {
            this._localVdomId = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_LOCAL_VDOM_ID_PREFIX, LEAF_LOCAL_VDOM_ID_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._localVdomId;
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
    @Override
    public se.dataductus.common.nso.path.MaapiPath _getPath() {
        if (super._getPath() == null && this.parent != null) {
            super._setPath(parent._getPath().go(PREFIX, TAG));
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
        this.serviceType()._unscheduledDelete();
        this.deploymentType()._unscheduledDelete();
        this.hostname()._unscheduledDelete();
        this.localVdomId()._unscheduledDelete();
        this.profile()._unscheduledDelete();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service _attach(com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service detached = this.parent;
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

    public Utm _detach() {
        this._attach((com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.Service)null);
        return this;
    }

    public Utm _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Utm _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.serviceType()._configureReader(reader);
            this.deploymentType()._configureReader(reader);
            this.hostname()._configureReader(reader);
            this.localVdomId()._configureReader(reader);
            this.profile()._configureReader(reader);

        }

        this.exists = true;
    }

    @Override
    public void _collectFromReader(se.dataductus.common.nso.path.MaapiReader reader, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.serviceType()._collectFromReader(reader);
            this.deploymentType()._collectFromReader(reader);
            this.hostname()._collectFromReader(reader);
            this.localVdomId()._collectFromReader(reader);
            this.profile()._collectFromReader(reader);

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

    public Utm _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
        this._doWrite(channel);
        return this;
    }

    @Override
    public void _configureWriter(se.dataductus.common.nso.path.MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (!this._exists() || this._hasScheduledDelete()) {
            parentWriter.go("dbutm", "utm").setDelete(true);
        }
        if (this._exists()) {
            se.dataductus.common.nso.path.MaapiWriter writer = parentWriter.go("dbutm", "utm");
            for (com.cisco.singtel.resourcemanager.base.DataNode child : _getScheduledWrites()) {
                child._configureWriter(writer);
            }
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.serviceType().exists()) {
            buffer.append(nextIndent).append("serviceType").append(": ").append(this.serviceType().get()).append(java.lang.System.lineSeparator());
        }
        if (this.deploymentType().exists()) {
            buffer.append(nextIndent).append("deploymentType").append(": ").append(this.deploymentType().get()).append(java.lang.System.lineSeparator());
        }
        if (this.hostname().exists()) {
            buffer.append(nextIndent).append("hostname").append(": ").append(this.hostname().get()).append(java.lang.System.lineSeparator());
        }
        if (this.localVdomId().exists()) {
            buffer.append(nextIndent).append("localVdomId").append(": ").append(this.localVdomId().get()).append(java.lang.System.lineSeparator());
        }
        if (this.profile().exists()) {
            buffer.append(nextIndent).append("profile").append(": ").append(this.profile().get()).append(java.lang.System.lineSeparator());
        }
    }
}
