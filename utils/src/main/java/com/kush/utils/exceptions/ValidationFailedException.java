package com.kush.utils.exceptions;

public class ValidationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ValidationFailedException() {
    }

    public ValidationFailedException(String message, Object... args) {
        super(String.format(message, args));
    }
}
