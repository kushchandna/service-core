package com.kush.lib.service.client.api;

public class RemoteServiceInvocationFailedException extends ServiceFailedException {

    private static final long serialVersionUID = 1L;

    public RemoteServiceInvocationFailedException() {
    }

    public RemoteServiceInvocationFailedException(Throwable e) {
        super(e);
    }
}
