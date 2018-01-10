package com.kush.lib.service.client.api;

public class NoSuchServiceClientExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchServiceClientExistsException() {
    }

    public NoSuchServiceClientExistsException(Class<? extends ServiceClient> serviceClientClass) {
        super("No service client found for class " + serviceClientClass.getName());
    }
}
