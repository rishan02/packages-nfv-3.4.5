package com.singtel.nfv.model.vcpe.createvcpe.input;

public enum Brand {
    CISCO("cisco"),
    FORTIGATE("fortigate");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, Brand> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(Brand.class);
    private static final java.util.Map<java.lang.Integer, Brand> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(Brand.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Brand> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Brand>() {
             @Override
             public Brand convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(Brand value) {
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
            public boolean exists(Brand value) {
                return value != null;
            }
        };

    Brand(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static Brand fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
