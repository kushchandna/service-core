package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.api.ServiceApi;

public abstract class ServiceClient<S extends ServiceApi> {

    private Responder responder;
    private S service;

    public void activate(Executor executor, ServiceApi serviceApi) {
        responder = new Responder(executor);
        service = getServiceApiClass().cast(serviceApi);
    }

    protected final <T> Response<T> invoke(ServiceTask<T> serviceTask) {
        return responder.invoke(serviceTask);
    }

    protected final S getService() {
        return service;
    }

    protected abstract Class<S> getServiceApiClass();
}
