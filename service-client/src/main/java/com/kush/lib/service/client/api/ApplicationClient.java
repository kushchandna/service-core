package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void start(ServiceConnectionFactory connectionFactory) {
        ServiceClientActivator activator = new ServiceClientActivator(connectionFactory);
        serviceClientProvider = new ServiceClientProvider(activator);
    }

    public void activateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        serviceClientProvider.activateServiceClient(serviceClientClass, executor);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
