package com.cisco.singtel.resourcemanager.base;

import com.tailf.conf.ConfException;
import se.dataductus.common.nso.channel.MaapiChannel;
import se.dataductus.common.nso.path.MaapiPath;
import se.dataductus.common.nso.path.MaapiReader;
import se.dataductus.common.nso.path.MaapiWriter;

import java.util.*;

public abstract class DataTreeNode extends PathNode implements DataNode {
    private SortedMap<DataNode, Boolean> scheduledWrites = null;
    private boolean scheduledDelete = false;

    public DataTreeNode(MaapiPath path) {
        super(path);
    }

    public boolean _exists(MaapiChannel channel) {
        return channel.exists(this._getPath());
    }

    public abstract void _configureReader(MaapiReader parentReader, MaapiChannel channel, Scope scope, EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode);
    public abstract void _collectFromReader(MaapiReader parentReader, Scope scope, EnumSet<com.cisco.singtel.resourcemanager.base.Mode> mode);

    public boolean _scheduleWrite(DataNode child) {
        if (this.scheduledWrites == null) {
            this.scheduledWrites = new TreeMap<>(this.createScheduleComparator());
        }

        boolean exists = child._exists();
        Boolean lastExists = this.scheduledWrites.put(child, exists);

        return lastExists == null || lastExists != exists;
    }

    public abstract void _configureWriter(MaapiWriter parentWriter) throws ConfException;

    @Override
    public void _clearScheduledWrites() {
        this._clearNestedScheduledWrites();

        this.scheduledDelete = false;
    }

    protected void _scheduleDelete() {
        this._clearNestedScheduledWrites();

        this.scheduledDelete = true;
    }

    protected boolean _hasScheduledDelete() {
        return this.scheduledDelete;
    }

    protected Set<DataNode> _getScheduledWrites() {
        return this.scheduledWrites != null ? this.scheduledWrites.keySet() : Collections.<DataNode>emptySet();
    }

    protected void _clearNestedScheduledWrites() {
        for (DataNode child : this._getScheduledWrites()) {
            child._clearScheduledWrites();
        }

        this.scheduledWrites = null;
    }

    protected Comparator<DataNode> createScheduleComparator() {
        return new Comparator<DataNode>() {
            @Override
            public int compare(DataNode lhs, DataNode rhs) {
                return lhs._getLabel().compareTo(rhs._getLabel());
            }
        };
    }

    protected void _doRead(MaapiChannel channel, Scope scope, EnumSet<Mode> mode) {
        MaapiReader reader = MaapiReader.fromPath(this._getPath().asKeyPath());
        this._configureReader(reader, channel, scope, mode);
        reader.read(channel);
        this._collectFromReader(reader, scope, mode);
    }

    protected void _doWrite(MaapiChannel channel) throws ConfException {
        MaapiWriter writer = MaapiWriter.fromPath(this._getPath().getParent().asKeyPath());
        this._configureWriter(writer);
        writer.write(channel);
        this._clearScheduledWrites();
    }
}
