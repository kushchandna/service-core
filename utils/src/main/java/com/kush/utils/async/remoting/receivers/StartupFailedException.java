package com.kush.utils.async.remoting.receivers;

public class StartupFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public StartupFailedException() {
    }

    public StartupFailedException(String message) {
        super(message);
    }

    public StartupFailedException(String message, Throwable e) {
        super(message, e);
    }
}
