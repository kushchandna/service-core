package com.kush.lib.service.server.core;

class ServiceRequestFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceRequestFailedException() {
    }

    public ServiceRequestFailedException(String message) {
        super(message);
    }

    public ServiceRequestFailedException(String message, Throwable e) {
        super(message, e);
    }
}
