package com.cisco.singtel.resourcemanager.base;

public enum Scope {
    NONE(0),
    LEAVES(1),
    ALL_EXCEPT_LISTS(2),
    ALL(3);

    private final int level;

    Scope(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
