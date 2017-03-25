package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm;

public enum DeploymentStatus {
    IN_PROGRESS("in-progress"),
    READY("ready"),
    FAILED("failed");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, DeploymentStatus> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(DeploymentStatus.class);
    private static final java.util.Map<java.lang.Integer, DeploymentStatus> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(DeploymentStatus.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<DeploymentStatus> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<DeploymentStatus>() {
             @Override
             public DeploymentStatus convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(DeploymentStatus value) {
                 if (value != null) {
                     try {
                         return new com.tailf.conf.ConfEnumeration(value.ordinal());
                     } catch (com.tailf.conf.ConfException e) {
                         // Return null below
                     }
                 }

                 return null;
             }

            @Override
            public boolean exists(DeploymentStatus value) {
                return value != null;
            }
        };

    DeploymentStatus(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static DeploymentStatus fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
