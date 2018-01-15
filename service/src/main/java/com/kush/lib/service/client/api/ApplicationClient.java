package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceApi;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void connect(ConnectionSpecification connSpec) {
        serviceClientProvider = new ServiceClientProvider(connSpec);
    }

    public void activateServiceClient(Class<? extends ServiceClient<? extends ServiceApi>> serviceClientClass,
            Executor executor) {
        serviceClientProvider.addServiceClient(serviceClientClass, executor);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
