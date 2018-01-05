package com.kush.lib.service.client.api;

public class ServiceFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceFailedException() {
    }

    public ServiceFailedException(Throwable e) {
        super(e.getMessage(), e);
    }
}
