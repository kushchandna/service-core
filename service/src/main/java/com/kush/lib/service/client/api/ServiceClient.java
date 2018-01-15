package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.RemoteServiceProvider;
import com.kush.lib.service.remoting.ServiceApi;

public abstract class ServiceClient<S extends ServiceApi> {

    private final String serviceName;
    private final Class<S> serviceApiClass;

    private Responder responder;
    private S serviceApi;

    public ServiceClient(String serviceName, Class<S> serviceApiClass) {
        this.serviceName = serviceName;
        this.serviceApiClass = serviceApiClass;
    }

    protected final <T> Response<T> invoke(ServiceTask<T> serviceTask) {
        return responder.invoke(serviceTask);
    }

    protected final S getService() {
        return serviceApi;
    }

    public final void activate(RemoteServiceProvider serviceProvider, Executor executor) {
        serviceApi = serviceApiClass.cast(serviceProvider.getRemoteService(serviceName));
        responder = new Responder(executor);
    }
}
