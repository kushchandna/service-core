package com.kush.lib.service.client.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.RemoteServiceProvider;
import com.kush.lib.service.remoting.ServiceApi;

public class ServiceClientProvider {

    private final Map<Class<? extends ServiceClient<?>>, ServiceClient<?>> serviceClients = new HashMap<>();
    private final RemoteServiceProvider serviceProvider;

    public ServiceClientProvider(ConnectionSpecification connSpec) {
        serviceProvider = connSpec.getRemoteServiceProvider();
    }

    public <S extends ServiceClient<? extends ServiceApi>> S getServiceClient(Class<S> serviceClientClass) {
        ServiceClient<?> serviceClient = serviceClients.get(serviceClientClass);
        if (serviceClient == null) {
            throw new NoSuchServiceClientExistsException(serviceClientClass);
        }
        return serviceClientClass.cast(serviceClient);
    }

    public void addServiceClient(Class<? extends ServiceClient<? extends ServiceApi>> serviceClientClass, Executor executor) {
        try {
            ServiceClient<? extends ServiceApi> clientInstance = serviceClientClass.newInstance();
            clientInstance.activate(serviceProvider, executor);
            serviceClients.put(serviceClientClass, clientInstance);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
