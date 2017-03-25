package com.cisco.singtel.resourcemanager.base;

import se.dataductus.common.nso.path.MaapiPath;

public interface ParameterNode {
    MaapiPath _getPath();
    String _getLabel();
    String _getPrefix();
    String _getTag();

    boolean _exists();
}