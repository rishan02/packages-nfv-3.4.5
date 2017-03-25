package com.cisco.singtel.resourcemanager.model.singtelrm.resourcemanager.pools.objectpool.objects.redeploy.output.commitqueue;

public enum Status {
    ASYNC("async"),
    TIMEOUT("timeout");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, Status> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(Status.class);
    private static final java.util.Map<java.lang.Integer, Status> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(Status.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Status> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Status>() {
             @Override
             public Status convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(Status value) {
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
            public boolean exists(Status value) {
                return value != null;
            }
        };

    Status(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static Status fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
