package com.kush.lib.service.server.authentication;

public class UserRegistrationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public UserRegistrationFailedException() {
    }

    public UserRegistrationFailedException(String message) {
        super(message);
    }

    public UserRegistrationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
