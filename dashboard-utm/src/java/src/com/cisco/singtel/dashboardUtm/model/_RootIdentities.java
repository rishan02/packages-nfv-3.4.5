package com.cisco.singtel.dashboardUtm.model;

public class _RootIdentities implements com.cisco.singtel.resourcemanager.base.Identities {
    public final static _RootIdentities INSTANCE = new _RootIdentities();

    private final java.util.Map<String, Object> identityMap = createIdentityMap();

    private static java.util.Map<String, Object> createIdentityMap() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();

        result.put("db:service-type-identity", com.cisco.singtel.dashboardUtm.model.db.ServiceTypeIdentity.Identity);
        result.put("dbutm:utm", com.cisco.singtel.dashboardUtm.model.dbutm.Utm.Identity);

        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getIdentity(String prefixedName, java.lang.Class<T> clazz) {
        Object identity = identityMap.get(prefixedName);
        return clazz.isInstance(identity) ? (T)identity : null;
    }

    private _RootIdentities() {
    }
}