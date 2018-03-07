package com.kush.lib.persistence.api;

public class PersistorOperationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistorOperationFailedException() {
    }

    public PersistorOperationFailedException(String message) {
        super(message);
    }

    public PersistorOperationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
