package com.kush.lib.service.client.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationClient {

    private final ExecutorService executor;

    private ServiceClientProvider serviceClientProvider;

    public ApplicationClient() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void connect(ConnectionSpecification connSpec) {
        serviceClientProvider = new ServiceClientProvider(executor, connSpec);
    }

    public void activateServiceClient(Class<? extends ServiceClient> serviceClientClass) {
        serviceClientProvider.addServiceClient(serviceClientClass);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
