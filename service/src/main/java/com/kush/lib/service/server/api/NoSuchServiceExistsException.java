package com.kush.lib.service.server.api;

public class NoSuchServiceExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchServiceExistsException() {
    }

    public NoSuchServiceExistsException(String serviceName) {
        super("No service found with name " + serviceName);
    }
}
