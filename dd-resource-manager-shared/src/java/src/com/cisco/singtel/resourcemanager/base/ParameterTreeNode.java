package com.cisco.singtel.resourcemanager.base;

import se.dataductus.common.nso.path.MaapiPath;

public abstract class ParameterTreeNode extends PathNode implements ParameterNode {

    public ParameterTreeNode(MaapiPath path) {
        super(path);
    }

    public abstract boolean _scheduleWrite(ParameterNode child);
}