package com.kush.lib.service.api.server;

public class ServiceInitializationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceInitializationFailedException() {
    }

    public ServiceInitializationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
