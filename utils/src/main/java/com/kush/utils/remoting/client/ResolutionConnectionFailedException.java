package com.kush.utils.remoting.client;

public class ResolutionConnectionFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ResolutionConnectionFailedException() {
    }

    public ResolutionConnectionFailedException(String message) {
        super(message);
    }

    public ResolutionConnectionFailedException(String message, Throwable e) {
        super(message, e);
    }
}
