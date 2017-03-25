package com.singtel.nfv.model.vcpe.createvcpe.input;

public enum NfvSetup {
    HOSTED("hosted"),
    EDGE("edge"),
    BRANCH("branch");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, NfvSetup> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(NfvSetup.class);
    private static final java.util.Map<java.lang.Integer, NfvSetup> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(NfvSetup.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<NfvSetup> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<NfvSetup>() {
             @Override
             public NfvSetup convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(NfvSetup value) {
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
            public boolean exists(NfvSetup value) {
                return value != null;
            }
        };

    NfvSetup(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static NfvSetup fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
