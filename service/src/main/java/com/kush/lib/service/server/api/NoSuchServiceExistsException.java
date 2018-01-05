package com.kush.lib.service.server.api;

public class NoSuchServiceExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchServiceExistsException() {
    }

    public NoSuchServiceExistsException(Class<? extends Service> serviceClass) {
        super("No service found for service class " + serviceClass.getName());
    }
}
