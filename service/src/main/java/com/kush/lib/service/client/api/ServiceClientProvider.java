package com.kush.lib.service.client.api;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceProvider;
import com.kush.lib.service.remoting.ServiceApi;

public class ServiceClientProvider {

    private final Map<Class<? extends ServiceClient<?>>, ServiceClient<?>> serviceClients = new HashMap<>();
    private final ServiceProvider serviceProvider;

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

    public <S extends ServiceApi, C extends ServiceClient<S>> void addServiceClient(Class<C> serviceClientClass,
            Class<S> serviceApiClass, Executor executor) {
        S serviceApi = serviceProvider.getService(serviceApiClass);
        try {
            ServiceClient<?> serviceClient = instantiateServiceClient(serviceClientClass, executor, serviceApi);
            serviceClients.put(serviceClientClass, serviceClient);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private <S extends ServiceApi, C extends ServiceClient<S>> C instantiateServiceClient(Class<C> serviceClientClass,
            Executor executor, S serviceApi) throws ReflectiveOperationException {
        Constructor<?>[] constructors = serviceClientClass.getConstructors();
        if (constructors.length > 1) {
            throw new IllegalArgumentException("Unsupported constructor found");
        }
        Object clientInstance = constructors[0].newInstance(executor, serviceApi);
        return serviceClientClass.cast(clientInstance);
    }
}
