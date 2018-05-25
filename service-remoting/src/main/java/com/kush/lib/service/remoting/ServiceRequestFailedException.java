package com.kush.lib.service.remoting;

import com.kush.utils.remoting.ResolutionFailedException;

public class ServiceRequestFailedException extends ResolutionFailedException {

    private static final long serialVersionUID = 1L;

    public ServiceRequestFailedException() {
    }

    public ServiceRequestFailedException(String message) {
        super(message);
    }

    public ServiceRequestFailedException(Throwable e) {
        super(e.getMessage(), e);
    }

    public ServiceRequestFailedException(String message, Throwable e) {
        super(message, e);
    }
}
