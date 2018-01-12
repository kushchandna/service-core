package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.ServiceApi;

public class NoSuchServiceExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchServiceExistsException() {
    }

    public NoSuchServiceExistsException(Class<? extends ServiceApi> serviceClass) {
        super("No service found with name " + serviceClass.getName());
    }
}
