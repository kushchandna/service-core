package com.kush.lib.service.remoting.connect;

public class ServiceConnectionFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServiceConnectionFailedException() {
    }

    public ServiceConnectionFailedException(String message) {
        super(message);
    }

    public ServiceConnectionFailedException(String message, Throwable e) {
        super(message, e);
    }
}
