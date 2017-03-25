package com.cisco.singtel.resourcemanager.base;

public interface Identities {
    public <T> T getIdentity(String prefixedName, Class<T> clazz);
}