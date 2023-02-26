package com.kush.lib.persistence.api;

public class PersistenceOperationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public PersistenceOperationFailedException() {
    }

    public PersistenceOperationFailedException(String message) {
        super(message);
    }

    public PersistenceOperationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
