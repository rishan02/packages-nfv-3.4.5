package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.deepchecksync;


public class Input extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "input";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "input";
    public static final java.lang.String LEAF_OUTFORMAT_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_OUTFORMAT_TAG = "outformat";
    public static final java.lang.String LEAF_SUPPRESS_POSITIVE_RESULT_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_SUPPRESS_POSITIVE_RESULT_TAG = "suppress-positive-result";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.OutformatDeepCheckSync> _outformat;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _suppressPositiveResult;

    public Input(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.DeepCheckSync parent) {
        super(null);
    }

    public Input(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: outformat</li>
     * <li> Yang type: outformat-deep-check-sync</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.OutformatDeepCheckSync> outformat() {
        if (this._outformat == null) {
            this._outformat = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.OutformatDeepCheckSync>(this, LEAF_OUTFORMAT_PREFIX, LEAF_OUTFORMAT_TAG, com.cisco.singtel.resourcemanager.model.ncs.OutformatDeepCheckSync.CONVERTER);
        }
        return this._outformat;
    }

    /**
     * Description:<br>
     * Use this additional parameter to only return services that<br>
     * failed to sync.<br>
     * <br>
     * tailf-info:<br>
     * Return list only contains negatives<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: suppress-positive-result</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> suppressPositiveResult() {
        if (this._suppressPositiveResult == null) {
            this._suppressPositiveResult = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_SUPPRESS_POSITIVE_RESULT_PREFIX, LEAF_SUPPRESS_POSITIVE_RESULT_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._suppressPositiveResult;
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
        this.outformat()._unscheduledDelete();
        this.suppressPositiveResult()._unscheduledDelete();
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
                case "singtel-rm:outformat": {
                    this.outformat()._unscheduledSet(param.getValue());
                    break;
                }
                case "singtel-rm:suppress-positive-result": {
                    this.suppressPositiveResult()._unscheduledSet(param.getValue());
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
        if (this.outformat()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.outformat()._getPrefix(), this.outformat()._getTag(), this.outformat().toConfValue()));
        }
        if (this.suppressPositiveResult()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.suppressPositiveResult()._getPrefix(), this.suppressPositiveResult()._getTag(), this.suppressPositiveResult().toConfValue()));
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.outformat().exists()) {
            buffer.append(nextIndent).append("outformat").append(": ").append(this.outformat().get()).append(java.lang.System.lineSeparator());
        }
        if (this.suppressPositiveResult().exists()) {
            buffer.append(nextIndent).append("suppressPositiveResult").append(": ").append(this.suppressPositiveResult().get()).append(java.lang.System.lineSeparator());
        }
    }
}
