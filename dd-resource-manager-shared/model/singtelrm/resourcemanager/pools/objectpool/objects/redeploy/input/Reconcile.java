package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.input;


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
public class Reconcile extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "reconcile";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "reconcile";
    public static final java.lang.String LEAF_C_NON_SERVICE_CONFIG_KEEP_NON_SERVICE_CONFIG_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_C_NON_SERVICE_CONFIG_KEEP_NON_SERVICE_CONFIG_TAG = "keep-non-service-config";
    public static final java.lang.String LEAF_C_NON_SERVICE_CONFIG_DISCARD_NON_SERVICE_CONFIG_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_C_NON_SERVICE_CONFIG_DISCARD_NON_SERVICE_CONFIG_TAG = "discard-non-service-config";
    private com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input parent;
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _keepNonServiceConfig;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _discardNonServiceConfig;
    private java.lang.String _choice_cNonServiceConfig_case;

    public Reconcile(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.Input parent) {
        super(null);
        this.parent = parent;
    }

    public Reconcile(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
        this.parent = null;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: keep-non-service-config</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> keepNonServiceConfig() {
        if (this._keepNonServiceConfig == null) {
            this._keepNonServiceConfig = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_C_NON_SERVICE_CONFIG_KEEP_NON_SERVICE_CONFIG_PREFIX, LEAF_C_NON_SERVICE_CONFIG_KEEP_NON_SERVICE_CONFIG_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._keepNonServiceConfig;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: discard-non-service-config</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> discardNonServiceConfig() {
        if (this._discardNonServiceConfig == null) {
            this._discardNonServiceConfig = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_C_NON_SERVICE_CONFIG_DISCARD_NON_SERVICE_CONFIG_PREFIX, LEAF_C_NON_SERVICE_CONFIG_DISCARD_NON_SERVICE_CONFIG_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._discardNonServiceConfig;
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
        this.keepNonServiceConfig()._unscheduledDelete();
        this.discardNonServiceConfig()._unscheduledDelete();
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

    public Reconcile _detach() {
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
                case "singtel-rm:keep-non-service-config": {
                    this.keepNonServiceConfig()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:discard-non-service-config": {
                    this.discardNonServiceConfig()._unscheduledSet(param.getValue());
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
        if (this.keepNonServiceConfig()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.keepNonServiceConfig()._getPrefix(), this.keepNonServiceConfig()._getTag(), this.keepNonServiceConfig().toConfValue()));
        }
        if (this.discardNonServiceConfig()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.discardNonServiceConfig()._getPrefix(), this.discardNonServiceConfig()._getTag(), this.discardNonServiceConfig().toConfValue()));
        }
        params.add(new com.tailf.conf.ConfXMLParamStop(this._getPrefix(), this._getTag()));
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.keepNonServiceConfig().exists()) {
            buffer.append(nextIndent).append("keepNonServiceConfig").append(": ").append(this.keepNonServiceConfig().get()).append(java.lang.System.lineSeparator());
        }
        if (this.discardNonServiceConfig().exists()) {
            buffer.append(nextIndent).append("discardNonServiceConfig").append(": ").append(this.discardNonServiceConfig().get()).append(java.lang.System.lineSeparator());
        }
    }
}
