package com.singtel.nfv.model.vcpe.createvcpesubinterfaces.output;

public enum Result {
    OK("ok"),
    NOK("nok");

    private final java.lang.String name;
    private static final java.util.Map<java.lang.String, Result> NAME_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createNameLookup(Result.class);
    private static final java.util.Map<java.lang.Integer, Result> ORDINAL_LOOKUP = com.cisco.singtel.resourcemanager.base.Enum.createOrdinalLookup(Result.class);

    public static com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Result> CONVERTER =
        new com.cisco.singtel.resourcemanager.base.Converters.ConfObjectConverter<Result>() {
             @Override
             public Result convertFrom(com.tailf.conf.ConfObject value) {
                 return value instanceof com.tailf.conf.ConfEnumeration ? ORDINAL_LOOKUP.get(((com.tailf.conf.ConfEnumeration)value).getOrdinalValue()) : null;
             }

             @Override
             public com.tailf.conf.ConfValue convertTo(Result value) {
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
            public boolean exists(Result value) {
                return value != null;
            }
        };

    Result(final java.lang.String name) {
        this.name = name;
    }

    @Override
    public java.lang.String toString() {
        return name;
    }

    public static Result fromString(java.lang.String name) {
        return NAME_LOOKUP.get(name);
    }
}
