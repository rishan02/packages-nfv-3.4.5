package com.cisco.singtel.resourcemanager.base;

import java.util.HashMap;
import java.util.Map;

public class Enum {

    private Enum() {
    }

    public static <T extends java.lang.Enum> Map<String, T> createNameLookup(Class<T> clazz) {
        Map<String, T> result = new HashMap<>();

        for (T item : clazz.getEnumConstants()) {
            result.put(item.toString(), item);
        }

        return result;
    }

    public static <T extends java.lang.Enum<T>> Map<Integer, T> createOrdinalLookup(Class<T> clazz) {
        Map<Integer, T> result = new HashMap<>();

        for (T item : clazz.getEnumConstants()) {
            result.put(item.ordinal(), item);
        }

        return result;
    }
}