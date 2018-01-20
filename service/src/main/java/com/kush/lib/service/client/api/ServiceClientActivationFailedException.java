package com.kush.lib.service.client.api;

public class ServiceClientActivationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceClientActivationFailedException() {
    }

    public ServiceClientActivationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
