package com.kush.lib.service.server;

public class ServiceInitializationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceInitializationFailedException() {
    }

    public ServiceInitializationFailedException(String message) {
        super(message);
    }

    public ServiceInitializationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
