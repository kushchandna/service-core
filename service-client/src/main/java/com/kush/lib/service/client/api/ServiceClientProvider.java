package com.kush.lib.service.client.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.kush.lib.service.client.internal.ServiceClientActivator;
import com.kush.utils.exceptions.ObjectNotFoundException;

public class ServiceClientProvider {

    private final Map<Class<? extends ServiceClient<?>>, ServiceClient<?>> serviceClients = new HashMap<>();

    private final ServiceClientActivator activator;

    public ServiceClientProvider(ServiceClientActivator activator) {
        this.activator = activator;
    }

    void activateServiceClient(Class<? extends ServiceClient<?>> serviceClientClass, String serviceName, Executor executor)
            throws ServiceClientActivationFailedException {
        ServiceClient<?> serviceClient = activator.activate(serviceClientClass, serviceName, executor);
        serviceClients.put(serviceClientClass, serviceClient);
    }

    public <S extends ServiceClient<?>> S getServiceClient(Class<S> serviceClientClass) throws ObjectNotFoundException {
        ServiceClient<?> serviceClient = serviceClients.get(serviceClientClass);
        if (serviceClient == null) {
            throw new ObjectNotFoundException("service client", serviceClientClass);
        }
        return serviceClientClass.cast(serviceClient);
    }
}
