package com.kush.lib.service.server.api;

public class ServiceInitializationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceInitializationFailedException() {
    }

    public ServiceInitializationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
