package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.internal.DefaultServiceClientActivator;
import com.kush.lib.service.client.internal.ServiceClientActivator;
import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.remoting.api.RemoteServiceProvider;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void connect(ConnectionSpecification connSpec) {
        RemoteServiceProvider serviceApiProvider = connSpec.getServiceProvider();
        ServiceClientActivator activator = new DefaultServiceClientActivator(serviceApiProvider);
        serviceClientProvider = new ServiceClientProvider(activator);
    }

    public void activateServiceClient(Class<? extends ServiceClient<?>> serviceClientClass, String serviceName, Executor executor)
            throws ServiceClientActivationFailedException {
        serviceClientProvider.activateServiceClient(serviceClientClass, serviceName, executor);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
