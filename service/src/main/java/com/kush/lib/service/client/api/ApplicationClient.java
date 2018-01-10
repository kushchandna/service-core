package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void connect(ConnectionSpecification connSpec) {
        serviceClientProvider = new ServiceClientProvider(connSpec);
    }

    public void activateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor) {
        serviceClientProvider.addServiceClient(serviceClientClass, executor);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
