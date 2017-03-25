package com.singtel.nfv.model.vcpe.deletevcpe;


public class Input extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "input";
    private static final java.lang.String PREFIX = "vcpe";
    private static final java.lang.String LABEL = "vcpe:input";
    public static final java.lang.String LEAF_SERVICE_NAME_PREFIX = "vcpe";
    public static final java.lang.String LEAF_SERVICE_NAME_TAG = "service-name";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> _serviceName;

    public Input(com.singtel.nfv.model.vcpe.DeleteVcpe parent) {
        super(null);
    }

    public Input(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * tailf-info:<br>
     * Name of the vcpe service<br>
     * <br>
     * Type information <br>
     * <ul>
     * <li> Yang name: service-name</li>
     * <li> Yang type: string</li>
     * <li> Ranges: 1..20</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String> serviceName() {
        if (this._serviceName == null) {
            this._serviceName = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.String>(this, LEAF_SERVICE_NAME_PREFIX, LEAF_SERVICE_NAME_TAG, com.cisco.singtel.resourcemanager.base.Converters.confBufConverter);
        }
        return this._serviceName;
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
        this.serviceName()._unscheduledDelete();
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
                case "vcpe:service-name": {
                    this.serviceName()._unscheduledSet(param.getValue());
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
        if (this.serviceName()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.serviceName()._getPrefix(), this.serviceName()._getTag(), this.serviceName().toConfValue()));
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.serviceName().exists()) {
            buffer.append(nextIndent).append("serviceName").append(": ").append(this.serviceName().get()).append(java.lang.System.lineSeparator());
        }
    }
}
