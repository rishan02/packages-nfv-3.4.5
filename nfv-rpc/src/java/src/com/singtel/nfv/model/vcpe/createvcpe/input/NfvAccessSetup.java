package com.singtel.nfv.model.vcpe.createvcpe.input;

public enum NfvAccessSetup {
    L2NID("l2nid"),
    L3NID("l3nid"),
    L3VPN("l3vpn");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, NfvAccessSetup> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(NfvAccessSetup.class);
    private static final java.util.Map<java.lang.Integer, NfvAccessSetup> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(NfvAccessSetup.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<NfvAccessSetup> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<NfvAccessSetup>() {
             @Override
             public NfvAccessSetup convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(NfvAccessSetup value) {
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
            public boolean exists(NfvAccessSetup value) {
                return value != null;
            }
        };

    NfvAccessSetup(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static NfvAccessSetup fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
