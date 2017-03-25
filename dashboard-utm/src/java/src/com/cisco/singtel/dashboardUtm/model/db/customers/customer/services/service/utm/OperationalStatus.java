package com.cisco.singtel.dashboardUtm.model.db.customers.customer.services.service.utm;

public enum OperationalStatus {
    IN_PROGRESS("in-progress"),
    READY("ready"),
    RECOVERING("recovering"),
    FAILED("failed");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, OperationalStatus> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(OperationalStatus.class);
    private static final java.util.Map<java.lang.Integer, OperationalStatus> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(OperationalStatus.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<OperationalStatus> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<OperationalStatus>() {
             @Override
             public OperationalStatus convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(OperationalStatus value) {
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
            public boolean exists(OperationalStatus value) {
                return value != null;
            }
        };

    OperationalStatus(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static OperationalStatus fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
