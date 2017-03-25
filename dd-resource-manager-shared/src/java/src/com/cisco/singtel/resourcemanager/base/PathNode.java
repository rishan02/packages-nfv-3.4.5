package com.cisco.singtel.resourcemanager.base;

import se.dataductus.common.nso.path.MaapiPath;

public abstract class PathNode {
    private MaapiPath path;

    public PathNode(MaapiPath path) {
        this.path = path;
    }

    public MaapiPath _getPath() {
        return this.path;
    }

    public void _setPath(MaapiPath path) {
        this.path = path;
    }

    public abstract String _getLabel();
    public abstract String _getPrefix();
    public abstract String _getTag();

    public String _formatTree() {
        StringBuilder buffer = new StringBuilder();
        this._formatTreeTo(buffer, "", "  ");
        return buffer.toString();
    }

    public abstract void _formatTreeTo(StringBuilder buffer, String indent, String indentStep);
}
