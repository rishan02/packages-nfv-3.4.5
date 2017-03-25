package com.singtel.nfv.model.vcpe.modifyvcpe;


public class Output extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "output";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:output";
    public static final java.lang.String LEAF_RESULT_PREFIX = "vcpe";
    public static final java.lang.String LEAF_RESULT_TAG = "result";
    public static final java.lang.String LEAF_SUCCESS_MESSAGE_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SUCCESS_MESSAGE_TAG = "success-message";
    public static final java.lang.String LEAF_ERRNO_PREFIX = "vcpe";
    public static final java.lang.String LEAF_ERRNO_TAG = "errno";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.output.Result> _result;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _successMessage;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.output.Errno> _errno;

    public Output(com.singtel.nfv.model.vcpe.ModifyVcpe parent) {
        super(null);
    }

    public Output(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: result</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.output.Result> result() {
        if (this._result == null) {
            this._result = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.output.Result>(this, LEAF_RESULT_PREFIX, LEAF_RESULT_TAG, com.singtel.nfv.model.vcpe.modifyvcpe.output.Result.CONVERTER);
        }
        return this._result;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: success-message</li>
     * <li> Yang type: string</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> successMessage() {
        if (this._successMessage == null) {
            this._successMessage = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_SUCCESS_MESSAGE_PREFIX, LEAF_SUCCESS_MESSAGE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._successMessage;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: errno</li>
     * <li> Yang type: enumeration</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.output.Errno> errno() {
        if (this._errno == null) {
            this._errno = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.singtel.nfv.model.vcpe.modifyvcpe.output.Errno>(this, LEAF_ERRNO_PREFIX, LEAF_ERRNO_TAG, com.singtel.nfv.model.vcpe.modifyvcpe.output.Errno.CONVERTER);
        }
        return this._errno;
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
        this.result()._unscheduledDelete();
        this.successMessage()._unscheduledDelete();
        this.errno()._unscheduledDelete();
    }

    public boolean _scheduleWrite(com.cisco.singtel.resourcemanager.base.ParameterNode child) {
        if (child._exists()) {
            this.exists = true;
        }

        return false;
    }

    public Output _fromConfXMLParams(com.tailf.conf.ConfXMLParam[] params) throws com.tailf.conf.ConfException {
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
                case "vcpe:result": {
                    this.result()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:success-message": {
                    this.successMessage()._unscheduledSet(param.getValue());
                    break;
                }
                case "vcpe:errno": {
                    this.errno()._unscheduledSet(param.getValue());
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
        if (this.result()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.result()._getPrefix(), this.result()._getTag(), this.result().toConfValue()));
        }
        if (this.successMessage()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.successMessage()._getPrefix(), this.successMessage()._getTag(), this.successMessage().toConfValue()));
        }
        if (this.errno()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.errno()._getPrefix(), this.errno()._getTag(), this.errno().toConfValue()));
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.result().exists()) {
            buffer.append(nextIndent).append("result").append(": ").append(this.result().get()).append(java.lang.System.lineSeparator());
        }
        if (this.successMessage().exists()) {
            buffer.append(nextIndent).append("successMessage").append(": ").append(this.successMessage().get()).append(java.lang.System.lineSeparator());
        }
        if (this.errno().exists()) {
            buffer.append(nextIndent).append("errno").append(": ").append(this.errno().get()).append(java.lang.System.lineSeparator());
        }
    }
}
