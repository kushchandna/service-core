package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceApi;

public abstract class ServiceClient<S extends ServiceApi> {

    private final Responder responder;
    private final S serviceApi;

    public ServiceClient(Executor executor, S serviceApi) {
        this.serviceApi = serviceApi;
        responder = new Responder(executor);
    }

    protected final <T> Response<T> invoke(ServiceTask<T> serviceTask) {
        return responder.invoke(serviceTask);
    }

    protected final S getService() {
        return serviceApi;
    }
}
