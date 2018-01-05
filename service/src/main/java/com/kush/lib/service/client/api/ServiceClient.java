package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

public abstract class ServiceClient {

    private final Responder responder;

    public ServiceClient(Executor executor) {
        responder = new Responder(executor);
    }

    protected <T> Response<T> invoke(ServiceTask<T> task) {
        return responder.invoke(task);
    }
}
