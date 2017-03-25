package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.getmodifications;


public class Input extends com.cisco.singtel.resourcemanager.base.ParameterTreeNode {
    private static final java.lang.String TAG = "input";
    private static final java.lang.String PREFIX = "singtel-rm";
    private static final java.lang.String LABEL = "input";
    public static final java.lang.String LEAF_OUTFORMAT_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_OUTFORMAT_TAG = "outformat";
    public static final java.lang.String LEAF_REVERSE_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_REVERSE_TAG = "reverse";
    public static final java.lang.String LEAF_DEPTH_DEEP_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEPTH_DEEP_TAG = "deep";
    public static final java.lang.String LEAF_DEPTH_SHALLOW_PREFIX = "singtel-rm";
    public static final java.lang.String LEAF_DEPTH_SHALLOW_TAG = "shallow";
    private boolean exists = false;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.Outformat2> _outformat;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _reverse;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _deep;
    private com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> _shallow;
    private java.lang.String _choice_depth_case;

    public Input(com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.GetModifications parent) {
        super(null);
    }

    public Input(se.dataductus.common.nso.path.MaapiPath path) {
        super(path);
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: outformat</li>
     * <li> Yang type: outformat2</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.Outformat2> outformat() {
        if (this._outformat == null) {
            this._outformat = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<com.cisco.singtel.resourcemanager.model.ncs.Outformat2>(this, LEAF_OUTFORMAT_PREFIX, LEAF_OUTFORMAT_TAG, com.cisco.singtel.resourcemanager.model.ncs.Outformat2.CONVERTER);
        }
        return this._outformat;
    }

    /**
     * Type information <br>
     * <ul>
     * <li> Yang name: reverse</li>
     * <li> Yang type: empty</li>
    * </ul><br>
     */
    public com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean> reverse() {
        if (this._reverse == null) {
            this._reverse = new com.cisco.singtel.resourcemanager.base.ParameterLeaf<java.lang.Boolean>(this, LEAF_REVERSE_PREFIX, LEAF_REVERSE_TAG, com.cisco.singtel.resourcemanager.base.Converters.confEmptyConverter);
        }
        return this._reverse;
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
        this.reverse()._unscheduledDelete();
        this.deep()._unscheduledDelete();
        this.shallow()._unscheduledDelete();
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
                case "singtel-rm:reverse": {
                    this.reverse()._unscheduledSet(param.getValue());
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
        if (this.reverse()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.reverse()._getPrefix(), this.reverse()._getTag(), this.reverse().toConfValue()));
        }
        if (this.deep()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.deep()._getPrefix(), this.deep()._getTag(), this.deep().toConfValue()));
        }
        if (this.shallow()._exists()) {
            params.add(new com.tailf.conf.ConfXMLParamValue(this.shallow()._getPrefix(), this.shallow()._getTag(), this.shallow().toConfValue()));
        }
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        buffer.append(indent).append(this._getLabel()).append(java.lang.System.lineSeparator());
        java.lang.String nextIndent = indent + indentStep;
        if (this.outformat().exists()) {
            buffer.append(nextIndent).append("outformat").append(": ").append(this.outformat().get()).append(java.lang.System.lineSeparator());
        }
        if (this.reverse().exists()) {
            buffer.append(nextIndent).append("reverse").append(": ").append(this.reverse().get()).append(java.lang.System.lineSeparator());
        }
        if (this.deep().exists()) {
            buffer.append(nextIndent).append("deep").append(": ").append(this.deep().get()).append(java.lang.System.lineSeparator());
        }
        if (this.shallow().exists()) {
            buffer.append(nextIndent).append("shallow").append(": ").append(this.shallow().get()).append(java.lang.System.lineSeparator());
        }
    }
}
