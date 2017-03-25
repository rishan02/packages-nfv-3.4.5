package com.cisco.singtel.resourcemanager.exceptions;

/**
 * Exception for resource allocation error when no free object can be found.
 */
public class ResourceAllocationNoFreeObjectException extends ResourceAllocationException {

    /**
     * Constructor
     * @param message Message
     */
    public ResourceAllocationNoFreeObjectException(String message) {
        super(message);
    }
}
