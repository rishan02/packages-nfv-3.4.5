package com.cisco.singtel.resourcemanager.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to build composite resource names and recover parts thereof.
 */
public class ResourceName {

    private static final char ESCAPE_CHAR = '#';
    private static final char SEPARATOR_CHAR = '_';

    private static final String ESCAPE = String.valueOf(ESCAPE_CHAR);
    private static final String SEPARATOR = String.valueOf(SEPARATOR_CHAR);

    /**
     * Create resource name from component parts
     * @param parts Parts to combine
     * @return Composite resource name
     */
    public static String create(String... parts) {
        return create(Arrays.asList(parts));
    }

    /**
     * Create resource name from component parts
     * @param parts Parts to combine
     * @return Composite resource name
     */
    public static String create(List<String> parts) {
        return String.join(SEPARATOR, parts.stream().map(ResourceName::escapePart).collect(Collectors.toList()));
    }

    /**
     * Recover componenent parts of a composite resource name
     * @param composite
     * @return
     */
    public static List<String> parts(String composite) {
        List<String> result = new ArrayList<>();
        StringBuilder partBuilder = new StringBuilder();
        boolean escaped = false;
        for (int i = 0, length = composite.length(); i < length; i++) {
            char c = composite.charAt(i);
            if (escaped) {
                partBuilder.append(c);
                escaped = false;
            } else {
                if (c == ESCAPE_CHAR) {
                    escaped = true;
                } else if (c == SEPARATOR_CHAR) {
                    result.add(partBuilder.toString());
                    partBuilder = new StringBuilder();
                } else {
                    partBuilder.append(c);
                }
            }
        }

        result.add(partBuilder.toString());

        return result;
    }

    private ResourceName() {
    }

    private static String escapePart(String s) {
        return s.replace(ESCAPE, ESCAPE+ESCAPE).replace(SEPARATOR, ESCAPE+SEPARATOR);
    }
}
