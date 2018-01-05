package com.kush.lib.service.client.api;

public class ResponseFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ResponseFailedException() {
    }

    public ResponseFailedException(Throwable e) {
        super(e.getMessage(), e);
    }
}
