package com.kush.lib.service.api.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public abstract class ServiceClient {

    private final Responder responder;

    public ServiceClient(Executor executor) {
        responder = new Responder(executor);
    }

    protected <T> Response<T> invoke(Callable<T> callable) {
        return responder.invoke(callable);
    }
}
