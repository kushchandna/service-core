package com.kush.utils.remoting.client;

public class ConnectionFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ConnectionFailedException() {
    }

    public ConnectionFailedException(String message) {
        super(message);
    }

    public ConnectionFailedException(String message, Throwable e) {
        super(message, e);
    }
}
