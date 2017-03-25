package com.singtel.nfv.model.vcpe.createvcpe.output;

public enum Errno {
    NO_SUCH_SITE("no-such-site"),
    NO_SUCH_POD("no-such-pod"),
    NO_SUCH_DCI("no-such-dci"),
    NO_SUCH_LINK_ENDPOINT("no-such-link-endpoint"),
    NO_SUCH_LINK_ENDPOINT_SERVICE_TYPE("no-such-link-endpoint-service-type"),
    VLAN_ID_EXHAUSTED("vlan-id-exhausted"),
    MGMT_IP_EXHAUSTED("mgmt-ip-exhausted"),
    UNEXPECTED_ERROR("unexpected-error");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, Errno> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(Errno.class);
    private static final java.util.Map<java.lang.Integer, Errno> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(Errno.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Errno> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Errno>() {
             @Override
             public Errno convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(Errno value) {
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
            public boolean exists(Errno value) {
                return value != null;
            }
        };

    Errno(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static Errno fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
