package com.kush.utils.async.remoting.receivers;

public class ShutdownFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ShutdownFailedException() {
    }

    public ShutdownFailedException(String message) {
        super(message);
    }

    public ShutdownFailedException(String message, Throwable e) {
        super(message, e);
    }
}
