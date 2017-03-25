package com.cisco.singtel.resourcemanager.base;

import com.tailf.conf.ConfException;
import se.dataductus.common.nso.path.MaapiPath;
import se.dataductus.common.nso.path.MaapiWriter;

public interface DataNode {
    MaapiPath _getPath();
    String _getLabel();
    String _getPrefix();
    String _getTag();

    boolean _exists();

    void _configureWriter(MaapiWriter parentWriter) throws ConfException;
    void _clearScheduledWrites();
}
