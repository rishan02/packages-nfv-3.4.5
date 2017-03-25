package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm;

public enum DeploymentType {
    VIRTUAL("virtual"),
    PHYSICAL("physical");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, DeploymentType> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(DeploymentType.class);
    private static final java.util.Map<java.lang.Integer, DeploymentType> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(DeploymentType.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<DeploymentType> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<DeploymentType>() {
             @Override
             public DeploymentType convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(DeploymentType value) {
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
            public boolean exists(DeploymentType value) {
                return value != null;
            }
        };

    DeploymentType(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static DeploymentType fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
