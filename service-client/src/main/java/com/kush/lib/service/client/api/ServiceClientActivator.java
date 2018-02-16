package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;

class ServiceClientActivator {

    private final ServiceRequestResolver requestResolver;

    public ServiceClientActivator(ServiceRequestResolver requestResolver) {
        this.requestResolver = requestResolver;
    }

    public <C extends ServiceClient> C activate(Class<C> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        C serviceClient = instantiateServiceClient(serviceClientClass);
        serviceClient.activate(executor, requestResolver);
        return serviceClient;
    }

    private <C extends ServiceClient> C instantiateServiceClient(Class<C> serviceClientClass)
            throws ServiceClientActivationFailedException {
        try {
            return serviceClientClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServiceClientActivationFailedException("Failed to activate service client " + serviceClientClass.getName(),
                    e);
        }
    }
}
