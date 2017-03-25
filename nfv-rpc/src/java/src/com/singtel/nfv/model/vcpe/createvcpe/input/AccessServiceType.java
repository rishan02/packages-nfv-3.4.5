package com.singtel.nfv.model.vcpe.createvcpe.input;

public enum AccessServiceType {
    MEGAPOP("megapop"),
    CPLUS("cplus"),
    SINGNET("singnet");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, AccessServiceType> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(AccessServiceType.class);
    private static final java.util.Map<java.lang.Integer, AccessServiceType> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(AccessServiceType.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<AccessServiceType> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<AccessServiceType>() {
             @Override
             public AccessServiceType convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(AccessServiceType value) {
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
            public boolean exists(AccessServiceType value) {
                return value != null;
            }
        };

    AccessServiceType(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static AccessServiceType fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
