package com.cisco.singtel.resourcemanager.base;

import com.tailf.conf.ConfException;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfValue;
import se.dataductus.common.nso.path.MaapiPath;

public class Leaf<TParent extends PathNode, TValue> extends PathNode {
    protected TParent parent;
    protected final boolean prefixRequired;
    private final String label;
    private final String prefix;
    private final String tag;
    private final Converters.ConfObjectConverter<TValue> converter;
    private TValue value = null;

    public Leaf(TParent parent, String prefix, String tag, Converters.ConfObjectConverter<TValue> converter) {
        super(null);
        this.parent = parent;
        this.prefixRequired = !parent._getPrefix().equals(prefix);
        this.label = this.prefixRequired ? prefix + ":" + tag : tag;
        this.prefix = prefix;
        this.tag = tag;
        this.converter = converter;
    }

    public TValue get() {
        return this.value;
    }

    public boolean exists() {
        return this.converter.exists(this.value);
    }

    public void set(TValue value) {
        this.value = value;
    }

    public void set(ConfObject value) {
        this.value = this.converter.convertFrom(value);
    }

    public void delete() {
        this.value = null;
    }

    public String label() {
        return this.label;
    }

    public String prefix() {
        return this.prefix;
    }

    public String tag() {
        return this.tag;
    }

    public MaapiPath _getPath() {
        if (super._getPath() == null) {
            super._setPath(this.prefixRequired
                ? this.parent._getPath().go(this.prefix(), this.tag())
                : this.parent._getPath().go(this.tag()));
        }

        return super._getPath();
    }

    public String _getLabel() {
        return this.label();
    }

    public String _getPrefix() {
        return this.prefix();
    }

    public String _getTag() {
        return this.tag();
    }

    public boolean _exists() {
        return this.exists();
    }

    public ConfValue toConfValue() throws ConfException {
        return this.converter.convertTo(this.value);
    }

    public void _formatTreeTo(java.lang.StringBuilder buffer, java.lang.String indent, java.lang.String indentStep) {
        if (this.get() instanceof java.util.List) {
            buffer.append(indent).append(this.label()).append(":");
            if (this.exists()) {
                String nextIndent = indent + indentStep;
                for (java.lang.Object elem : (java.util.List) (this.get())) {
                    buffer.append(java.lang.System.lineSeparator()).append(nextIndent).append(elem);
                }
            } else {
                buffer.append(" null");
            }

            buffer.append(java.lang.System.lineSeparator());
        } else if (this.exists()) {
            buffer.append(indent).append(this.label()).append(": ").append(this.get()).append(java.lang.System.lineSeparator());
        }
    }
}