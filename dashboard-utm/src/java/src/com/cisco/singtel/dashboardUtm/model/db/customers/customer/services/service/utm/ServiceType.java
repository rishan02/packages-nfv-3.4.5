package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm;

public enum ServiceType {
    SINGNET("singnet"),
    VPN("vpn");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, ServiceType> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(ServiceType.class);
    private static final java.util.Map<java.lang.Integer, ServiceType> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(ServiceType.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<ServiceType> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<ServiceType>() {
             @Override
             public ServiceType convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(ServiceType value) {
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
            public boolean exists(ServiceType value) {
                return value != null;
            }
        };

    ServiceType(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static ServiceType fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
