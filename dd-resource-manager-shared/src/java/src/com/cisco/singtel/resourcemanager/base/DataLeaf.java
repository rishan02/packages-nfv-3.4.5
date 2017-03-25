package com.cisco.singtel.resourcemanager.base;

import com.tailf.conf.ConfObject;
import se.dataductus.common.nso.channel.MaapiChannel;
import se.dataductus.common.nso.path.MaapiReader;
import se.dataductus.common.nso.path.MaapiWriter;

public class DataLeaf<T> extends Leaf<DataTreeNode, T> implements DataNode {

    public DataLeaf(DataTreeNode parent, String prefix, String tag, Converters.ConfObjectConverter<T> converter) {
        super(parent, prefix, tag, converter);
    }

    @Override
    public void set(T value) {
        super.set(value);
        this.parent._scheduleWrite(this);
    }

    @Override
    public void set(ConfObject value) {
        super.set(value);
        this.parent._scheduleWrite(this);
    }

    @Override
    public void delete() {
        super.delete();
        this.parent._scheduleWrite(this);
    }

    public boolean exists(MaapiChannel channel) {
        return channel.exists(this._getPath());
    }

    public boolean _exists(MaapiChannel channel) {
        return this.exists(channel);
    }

    public DataLeaf<T> _read(MaapiChannel channel) {
        super.set(channel.getElem(this._getPath()));
        return this;
    }

    public void _configureReader(MaapiReader reader) {
        reader.get(this.label());
    }

    public void _collectFromReader(MaapiReader reader) {
        super.set(reader.get(this.label()));
    }

    @Override
    public void _configureWriter(MaapiWriter parentWriter) throws com.tailf.conf.ConfException {
        if (this.exists()) {
            parentWriter.set(this.label(), this.toConfValue());
        } else {
            parentWriter.deleteLeaf(this.label());
        }
    }

    @Override
    public void _clearScheduledWrites() {}

    public void _unscheduledSet(T value) {
        super.set(value);
    }

    public void _unscheduledSet(ConfObject value) {
        super.set(value);
    }

    public void _unscheduledDelete() {
        super.delete();
    }
}