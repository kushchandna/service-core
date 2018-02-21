package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceRequestResolver;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void connect(ConnectionSpecification connSpec) {
        ServiceRequestResolver requestResolver = connSpec.getResolver();
        ServiceClientActivator activator = new ServiceClientActivator(requestResolver);
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
