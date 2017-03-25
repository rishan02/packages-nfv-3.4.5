package com.cisco.singtel.resourcemanager.exceptions;

/**
 * Exception for resource allocation errors.
 */
public class ResourceAllocationException extends IllegalStateException {

    /**
     * Constructor
     * @param message Message
     */
    public ResourceAllocationException(String message) {
        super(message);
    }
}
