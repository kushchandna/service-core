package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceApi;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void connect(ConnectionSpecification connSpec) {
        serviceClientProvider = new ServiceClientProvider(connSpec);
    }

    public <S extends ServiceApi, C extends ServiceClient<S>> void activateServiceClient(Class<C> serviceClientClass,
            Class<S> serviceApiClass, Executor executor) {
        serviceClientProvider.addServiceClient(serviceClientClass, serviceApiClass, executor);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
