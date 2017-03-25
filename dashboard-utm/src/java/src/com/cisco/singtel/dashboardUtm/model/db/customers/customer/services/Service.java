package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services;


public class Service extends com.cisco.singtel.resourcemanager.base.DataTreeNode {
    private static final java.lang.String TAG = "service";
    private static final java.lang.String PREFIX = "db";
    private static final java.lang.String LABEL = "service";
    public static final java.lang.String LEAF_SERVICE_NAME_PREFIX = "db";
    public static final java.lang.String LEAF_SERVICE_NAME_TAG = "service-name";
    public static final java.lang.String LEAF_NFV_VAS_REFERENCE_PREFIX = "db";
    public static final java.lang.String LEAF_NFV_VAS_REFERENCE_TAG = "nfv-vas-reference";
    public static final java.lang.String LEAF_BAN_PREFIX = "db";
    public static final java.lang.String LEAF_BAN_TAG = "ban";
    public static final java.lang.String LEAF_PRODUCT_CODE_PREFIX = "db";
    public static final java.lang.String LEAF_PRODUCT_CODE_TAG = "product-code";
    public static final java.lang.String LEAF_SERVICE_TYPE_PREFIX = "db";
    public static final java.lang.String LEAF_SERVICE_TYPE_TAG = "service-type";
    public static final java.lang.String LEAF_NETWORK_PREFIX = "db";
    public static final java.lang.String LEAF_NETWORK_TAG = "network";
    public static final java.lang.String LEAF_DC_PREFIX = "db";
    public static final java.lang.String LEAF_DC_TAG = "dc";
    public static final java.lang.String LEAF_NFV_VAS_SCHEME_PREFIX = "db";
    public static final java.lang.String LEAF_NFV_VAS_SCHEME_TAG = "nfv-vas-scheme";
    public static final java.lang.String LEAF_BRAND_PREFIX = "db";
    public static final java.lang.String LEAF_BRAND_TAG = "brand";
    public static final java.lang.String LEAF_NFV_SETUP_PREFIX = "db";
    public static final java.lang.String LEAF_NFV_SETUP_TAG = "nfv-setup";
    public static final java.lang.String LEAF_NFV_ACCESS_SETUP_PREFIX = "db";
    public static final java.lang.String LEAF_NFV_ACCESS_SETUP_TAG = "nfv-access-setup";
    public static final java.lang.String LEAF_CIRCUIT_REFERENCE_PREFIX = "db";
    public static final java.lang.String LEAF_CIRCUIT_REFERENCE_TAG = "circuit-reference";
    public static final java.lang.String LEAF_SPEED_PREFIX = "db";
    public static final java.lang.String LEAF_SPEED_TAG = "speed";
    public static final java.lang.String LEAF_DEPLOY_STATUS_PREFIX = "db";
    public static final java.lang.String LEAF_DEPLOY_STATUS_TAG = "deploy-status";
    public static final java.lang.String LEAF_OPERATIONAL_STATUS_PREFIX = "db";
    public static final java.lang.String LEAF_OPERATIONAL_STATUS_TAG = "operational-status";
    private com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList parent;
    private boolean exists = false;
    private ServiceListKey key;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _serviceName;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _nfvVasReference;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _ban;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _productCode;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.ServiceTypeIdentity> _serviceType;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _network;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _dc;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _nfvVasScheme;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _brand;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _nfvSetup;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _nfvAccessSetup;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _circuitReference;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Long> _speed;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _deployStatus;
    private com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> _operationalStatus;
    private com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm _utm;

    public Service(com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList parent, ServiceListKey key) {
        super(null);
        this.parent = parent;
        this._setKey(key);
    }

    public Service(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
        this._setKey(new ServiceListKey(path.getKey()));
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: service-name</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> serviceName() {
        if (this._serviceName == null) {
            this._serviceName = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_SERVICE_NAME_PREFIX, LEAF_SERVICE_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._serviceName;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-vas-reference</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> nfvVasReference() {
        if (this._nfvVasReference == null) {
            this._nfvVasReference = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NFV_VAS_REFERENCE_PREFIX, LEAF_NFV_VAS_REFERENCE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._nfvVasReference;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: ban</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> ban() {
        if (this._ban == null) {
            this._ban = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_BAN_PREFIX, LEAF_BAN_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._ban;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: product-code</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> productCode() {
        if (this._productCode == null) {
            this._productCode = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_PRODUCT_CODE_PREFIX, LEAF_PRODUCT_CODE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._productCode;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: service-type</li>
     * <li> Yang type: identityref</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.ServiceTypeIdentity> serviceType() {
        if (this._serviceType == null) {
            this._serviceType = new com.cisco.singtel.resourcemanager.base.DataLeaf<com.cisco.singtel.dashboardUtm.model.db.ServiceTypeIdentity>(this, LEAF_SERVICE_TYPE_PREFIX, LEAF_SERVICE_TYPE_TAG, new com.cisco.singtel.resourcemanager.base.Converters.ConfIdentityRefConverter<com.cisco.singtel.dashboardUtm.model.db.ServiceTypeIdentity>(com.cisco.singtel.dashboardUtm.model._RootIdentities.INSTANCE, com.cisco.singtel.dashboardUtm.model.db.ServiceTypeIdentity.class));
        }
        return this._serviceType;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: network</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> network() {
        if (this._network == null) {
            this._network = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NETWORK_PREFIX, LEAF_NETWORK_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._network;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: dc</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> dc() {
        if (this._dc == null) {
            this._dc = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_DC_PREFIX, LEAF_DC_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._dc;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-vas-scheme</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> nfvVasScheme() {
        if (this._nfvVasScheme == null) {
            this._nfvVasScheme = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NFV_VAS_SCHEME_PREFIX, LEAF_NFV_VAS_SCHEME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._nfvVasScheme;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: brand</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> brand() {
        if (this._brand == null) {
            this._brand = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_BRAND_PREFIX, LEAF_BRAND_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._brand;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-setup</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> nfvSetup() {
        if (this._nfvSetup == null) {
            this._nfvSetup = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NFV_SETUP_PREFIX, LEAF_NFV_SETUP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._nfvSetup;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: nfv-access-setup</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> nfvAccessSetup() {
        if (this._nfvAccessSetup == null) {
            this._nfvAccessSetup = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_NFV_ACCESS_SETUP_PREFIX, LEAF_NFV_ACCESS_SETUP_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._nfvAccessSetup;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: circuit-reference</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> circuitReference() {
        if (this._circuitReference == null) {
            this._circuitReference = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_CIRCUIT_REFERENCE_PREFIX, LEAF_CIRCUIT_REFERENCE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._circuitReference;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: speed</li>
     * <li> Yang type: uint32</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Long> speed() {
        if (this._speed == null) {
            this._speed = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.Long>(this, LEAF_SPEED_PREFIX, LEAF_SPEED_TAG, com.cisco.singtel.resourcemanager.base.Converters.confUInt32Converter);
        }
        return this._speed;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: deploy-status</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> deployStatus() {
        if (this._deployStatus == null) {
            this._deployStatus = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_DEPLOY_STATUS_PREFIX, LEAF_DEPLOY_STATUS_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._deployStatus;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: operational-status</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String> operationalStatus() {
        if (this._operationalStatus == null) {
            this._operationalStatus = new com.cisco.singtel.resourcemanager.base.DataLeaf<java.lang.String>(this, LEAF_OPERATIONAL_STATUS_PREFIX, LEAF_OPERATIONAL_STATUS_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._operationalStatus;
    }

    /**
     * Presence:<br>
     * true<br>
     */
    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm utm() {
        if (this._utm == null) {
            this._utm = new com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm(this);
        }
        return this._utm;
    }

    public ServiceListKey _getKey() {
        return this.key;
    }

    protected void _setKey(ServiceListKey key) {
        this.nfvVasReference()._unscheduledSet(key.nfvVasReference());

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
        this.serviceName()._unscheduledDelete();
        this.nfvVasReference()._unscheduledDelete();
        this.ban()._unscheduledDelete();
        this.productCode()._unscheduledDelete();
        this.serviceType()._unscheduledDelete();
        this.network()._unscheduledDelete();
        this.dc()._unscheduledDelete();
        this.nfvVasScheme()._unscheduledDelete();
        this.brand()._unscheduledDelete();
        this.nfvSetup()._unscheduledDelete();
        this.nfvAccessSetup()._unscheduledDelete();
        this.circuitReference()._unscheduledDelete();
        this.speed()._unscheduledDelete();
        this.deployStatus()._unscheduledDelete();
        this.operationalStatus()._unscheduledDelete();
        this.utm()._detach();
        this._scheduleDelete();
        this.parent._scheduleWrite(this);
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList _parent() {
        if (this.parent == null) {
            this.parent = new com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList(this._getPath().getParent());
            this.parent._attach(this);
        }

        return this.parent;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm _attach(com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm child) {
        if (this._utm != child) {
            if (this._utm != null) {
                com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm detached = this._utm;
                this._utm = null;
                detached._detach();
            }

            this._utm = child;

            if (child != null) {
                child._attach(this);
            }
        }

        return child;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm _detach(com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm child) {
        com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm detached = this._utm;
        this._attach((com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.Utm)null);
        return detached;
    }

    public com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList _attach(com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList parent) {
        if (this.parent != parent) {
            if (this.parent != null) {
                com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList detached = this.parent;
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

    public Service _detach() {
        this._attach((com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.ServiceList)null);
        return this;
    }

    public Service _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope) {
        return this._read(channel, scope, java.util.EnumSet.of(com.cisco.singtel.resourcemanager.base.Mode.CONFIG));
    }

    public Service _read(se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        this._doRead(channel, scope, mode);
        return this;
    }

    @Override
    public void _configureReader(se.dataductus.common.nso.path.MaapiReader reader, se.dataductus.common.nso.channel.MaapiChannel channel, com.cisco.singtel.resourcemanager.base.Scope scope, java.util.EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode) {
        if (scope == com.cisco.singtel.resourcemanager.base.Scope.NONE) {
            return;
        }

        if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
            this.serviceName()._configureReader(reader);
            this.ban()._configureReader(reader);
            this.productCode()._configureReader(reader);
            this.serviceType()._configureReader(reader);
            this.network()._configureReader(reader);
            this.dc()._configureReader(reader);
            this.nfvVasScheme()._configureReader(reader);
            this.brand()._configureReader(reader);
            this.nfvSetup()._configureReader(reader);
            this.nfvAccessSetup()._configureReader(reader);
            this.circuitReference()._configureReader(reader);
            this.speed()._configureReader(reader);
            this.deployStatus()._configureReader(reader);
            this.operationalStatus()._configureReader(reader);

        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {

            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                if (channel.exists(utm()._getPath())) {
                    this.utm()._configureReader(reader.go("dbutm", "utm"), channel, scope, mode);
                }
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
            this.serviceName()._collectFromReader(reader);
            this.ban()._collectFromReader(reader);
            this.productCode()._collectFromReader(reader);
            this.serviceType()._collectFromReader(reader);
            this.network()._collectFromReader(reader);
            this.dc()._collectFromReader(reader);
            this.nfvVasScheme()._collectFromReader(reader);
            this.brand()._collectFromReader(reader);
            this.nfvSetup()._collectFromReader(reader);
            this.nfvAccessSetup()._collectFromReader(reader);
            this.circuitReference()._collectFromReader(reader);
            this.speed()._collectFromReader(reader);
            this.deployStatus()._collectFromReader(reader);
            this.operationalStatus()._collectFromReader(reader);

        }

        if (scope != com.cisco.singtel.resourcemanager.base.Scope.LEAVES) {
            if (mode.contains(com.cisco.singtel.resourcemanager.base.Mode.OPER)) {
                if (utm()._exists()) {
                    this.utm()._collectFromReader(reader.go("dbutm", "utm"), scope, mode);
                }
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

    public Service _write(se.dataductus.common.nso.channel.MaapiChannel channel) throws com.tailf.conf.ConfException {
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
        if (this.serviceName().exists()) {
            buffer.append(nextIndent).append("serviceName").append(": ").append(this.serviceName().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvVasReference().exists()) {
            buffer.append(nextIndent).append("nfvVasReference").append(": ").append(this.nfvVasReference().get()).append(java.lang.System.lineSeparator());
        }
        if (this.ban().exists()) {
            buffer.append(nextIndent).append("ban").append(": ").append(this.ban().get()).append(java.lang.System.lineSeparator());
        }
        if (this.productCode().exists()) {
            buffer.append(nextIndent).append("productCode").append(": ").append(this.productCode().get()).append(java.lang.System.lineSeparator());
        }
        if (this.serviceType().exists()) {
            buffer.append(nextIndent).append("serviceType").append(": ").append(this.serviceType().get()).append(java.lang.System.lineSeparator());
        }
        if (this.network().exists()) {
            buffer.append(nextIndent).append("network").append(": ").append(this.network().get()).append(java.lang.System.lineSeparator());
        }
        if (this.dc().exists()) {
            buffer.append(nextIndent).append("dc").append(": ").append(this.dc().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvVasScheme().exists()) {
            buffer.append(nextIndent).append("nfvVasScheme").append(": ").append(this.nfvVasScheme().get()).append(java.lang.System.lineSeparator());
        }
        if (this.brand().exists()) {
            buffer.append(nextIndent).append("brand").append(": ").append(this.brand().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvSetup().exists()) {
            buffer.append(nextIndent).append("nfvSetup").append(": ").append(this.nfvSetup().get()).append(java.lang.System.lineSeparator());
        }
        if (this.nfvAccessSetup().exists()) {
            buffer.append(nextIndent).append("nfvAccessSetup").append(": ").append(this.nfvAccessSetup().get()).append(java.lang.System.lineSeparator());
        }
        if (this.circuitReference().exists()) {
            buffer.append(nextIndent).append("circuitReference").append(": ").append(this.circuitReference().get()).append(java.lang.System.lineSeparator());
        }
        if (this.speed().exists()) {
            buffer.append(nextIndent).append("speed").append(": ").append(this.speed().get()).append(java.lang.System.lineSeparator());
        }
        if (this.deployStatus().exists()) {
            buffer.append(nextIndent).append("deployStatus").append(": ").append(this.deployStatus().get()).append(java.lang.System.lineSeparator());
        }
        if (this.operationalStatus().exists()) {
            buffer.append(nextIndent).append("operationalStatus").append(": ").append(this.operationalStatus().get()).append(java.lang.System.lineSeparator());
        }
        if (this.utm()._exists()) {
            this.utm()._formatTreeTo(buffer, nextIndent, indentStep);
        }
    }
}
