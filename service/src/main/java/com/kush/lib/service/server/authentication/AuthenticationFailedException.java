package com.kush.lib.service.server.authentication;

public class AuthenticationFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public AuthenticationFailedException() {
    }

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, Throwable e) {
        super(message, e);
    }
}
