package com.cisco.singtel.resourcemanager.util;



import com.google.common.collect.Lists;
import com.tailf.conf.ConfKey;
import com.tailf.conf.ConfObject;
import com.tailf.conf.ConfPath;
import com.tailf.conf.ConfTag;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Methods for {@link com.tailf.conf} package.
 */
public class ConfUtil {
    private ConfUtil() {
    }

    /**
     * Find key in keypath given the tag of the containing list, or throw
     * exception with useful error message if not found.
     * @param tag list tag
     * @param kp keypath
     * @return key if found
     * @throws NoSuchElementException if not found
     */
    public static ConfKey findKeyOrThrow(String tag, ConfObject[] kp) {
        return findKey(tag, kp).orElseThrow(
                () -> new NoSuchElementException(format("No key for %s in %s", tag, new ConfPath(kp))));
    }

    /**
     * Find key in keypath given the tag of the containing list.
     * @param tag list tag
     * @param kp keypath
     * @return {@link Optional} containing key or empty if not found.
     */
    private static Optional<ConfKey> findKey(String tag, ConfObject[] kp) {
        boolean previousWasTag = false;
        for (ConfObject e: Lists.reverse(asList(kp))) {
            if (previousWasTag) {
                if (e instanceof ConfKey) {
                    return Optional.of((ConfKey) e);
                } else {
                    break;
                }
            }
            if (e instanceof ConfTag) {
                ConfTag t = (ConfTag) e;
                if (tag.equals(t.getTag())) {
                    previousWasTag = true;
                }
            }
        }
        return Optional.empty();
    }
}