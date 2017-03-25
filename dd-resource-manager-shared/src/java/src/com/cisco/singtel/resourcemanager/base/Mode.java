package com.cisco.singtel.resourcemanager.base;

import java.util.EnumSet;

public enum Mode {
    CONFIG,
    OPER;
    public static final EnumSet<Mode> ALL = EnumSet.allOf(Mode.class);
}
