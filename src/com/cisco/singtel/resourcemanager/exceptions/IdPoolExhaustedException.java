package com.cisco.singtel.resourcemanager.exceptions;

/**
 * Exception for id pool exhausted.
 */
public class IdPoolExhaustedException extends ResourceAllocationException {

    /**
     * Constructor
     * @param message Message
     */
    public IdPoolExhaustedException(String message) {
        super(message);
    }
}
