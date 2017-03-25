package com.cisco.singtel.resourcemanager.util;

import com.tailf.ncs.template.TemplateVariables;

/**
 * Better template variables
 */
public class BetterTemplateVariables extends TemplateVariables {

    /**
     * Use with mandatory values. Exception will be thrown on null value.
     * @param key Key
     * @param value Value will be converted toString.
     * @return Previous value
     */
    public Object putQuoted(String key, Object value) {
        return super.putQuoted(key, value.toString());
    }

    /**
     * Use with non-mandatory values, when "" is ok as a value.
     * @param key Key
     * @param value Value. If null then "" will be put.
     * @return Previous value
     */
    public Object putQuotedEmptyIfNull(String key, String value) {
        return super.putQuoted(key, value == null ? "" : value);
    }

    /**
     * Use with non-mandatory values, when "" is ok as a value.
     * @param key Key
     * @param value Value will be converted toString. If null then "" will be put.
     * @return Previous value
     */
    public Object putQuotedEmptyIfNull(String key, Object value) {
        return super.putQuoted(key, value == null ? "" : value.toString());
    }
}
