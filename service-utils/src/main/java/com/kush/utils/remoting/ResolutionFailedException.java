package com.kush.utils.remoting;

public class ResolutionFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ResolutionFailedException() {
    }

    public ResolutionFailedException(Throwable e) {
        this(e.getMessage(), e);
    }

    public ResolutionFailedException(String message) {
        super(message);
    }

    public ResolutionFailedException(String message, Throwable e) {
        super(message, e);
    }
}
