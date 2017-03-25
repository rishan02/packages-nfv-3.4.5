package com.singtel.nfv.model.vcpe.createvcpe.input;

public enum NfvVasScheme {
    BASIC("basic"),
    ADVANCED("advanced"),
    PREMIUM("premium");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, NfvVasScheme> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(NfvVasScheme.class);
    private static final java.util.Map<java.lang.Integer, NfvVasScheme> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(NfvVasScheme.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<NfvVasScheme> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<NfvVasScheme>() {
             @Override
             public NfvVasScheme convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(NfvVasScheme value) {
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
            public boolean exists(NfvVasScheme value) {
                return value != null;
            }
        };

    NfvVasScheme(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static NfvVasScheme fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
