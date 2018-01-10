package com.kush.lib.service.client.api;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class ServiceClientProvider {

    private final Map<Class<? extends ServiceClient>, ServiceClient> serviceClients = new HashMap<>();
    private final ServiceInvoker serviceInvoker;

    public ServiceClientProvider(ConnectionSpecification connSpec) {
        serviceInvoker = connSpec.getServiceInvoker();
    }

    public void addServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor) {
        try {
            ServiceClient serviceClient = instantiateServiceClient(serviceClientClass, executor);
            serviceClients.put(serviceClientClass, serviceClient);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private ServiceClient instantiateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor)
            throws ReflectiveOperationException {
        Constructor<? extends ServiceClient> serviceClientConstructor =
                serviceClientClass.getConstructor(Executor.class, ServiceInvoker.class);
        return serviceClientConstructor.newInstance(executor, serviceInvoker);
    }

    public <S extends ServiceClient> S getServiceClient(Class<S> serviceClientClass) {
        ServiceClient serviceClient = serviceClients.get(serviceClientClass);
        if (serviceClient == null) {
            throw new NoSuchServiceClientExistsException(serviceClientClass);
        }
        return serviceClientClass.cast(serviceClient);
    }
}
