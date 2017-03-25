package com.singtel.nfv.model.vcpe.modifyvcpe.input;

public enum RoutingProtocol {
    STATIC("static"),
    BGP("bgp");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, RoutingProtocol> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(RoutingProtocol.class);
    private static final java.util.Map<java.lang.Integer, RoutingProtocol> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(RoutingProtocol.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<RoutingProtocol> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<RoutingProtocol>() {
             @Override
             public RoutingProtocol convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(RoutingProtocol value) {
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
            public boolean exists(RoutingProtocol value) {
                return value != null;
            }
        };

    RoutingProtocol(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static RoutingProtocol fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
