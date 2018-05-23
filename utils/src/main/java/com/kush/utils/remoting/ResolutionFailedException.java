package com.kush.utils.remoting;

public class ResolutionFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public ResolutionFailedException() {
    }

    public ResolutionFailedException(Throwable e) {
        super(e.getMessage(), e);
    }
}
