package com.kush.lib.service.api.server;

public class NoSuchServiceExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchServiceExistsException() {
    }

    public NoSuchServiceExistsException(Class<? extends Service> serviceClass) {
        super("No service found for service class " + serviceClass.getName());
    }
}
