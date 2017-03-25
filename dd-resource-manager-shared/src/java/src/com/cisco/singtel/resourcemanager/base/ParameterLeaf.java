package com.cisco.singtel.resourcemanager.base;

import com.tailf.conf.ConfObject;

public class ParameterLeaf<T> extends Leaf<ParameterTreeNode, T> implements ParameterNode {

    public ParameterLeaf(ParameterTreeNode parent, String prefix, String tag, Converters.ConfObjectConverter<T> converter) {
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