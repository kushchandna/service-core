package com.kush.lib.service.server.api;

public class ServiceOperationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceOperationFailedException() {
    }

    public ServiceOperationFailedException(String message) {
        super(message);
    }
}
