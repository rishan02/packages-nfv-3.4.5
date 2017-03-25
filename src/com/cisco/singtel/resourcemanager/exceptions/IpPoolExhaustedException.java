package com.cisco.singtel.resourcemanager.exceptions;

/**
 * Exception for ip pool exhausted.
 */
public class IpPoolExhaustedException extends ResourceAllocationException {

    /**
     * Constructor
     * @param message Message
     */
    public IpPoolExhaustedException(String message) {
        super(message);
    }
}
