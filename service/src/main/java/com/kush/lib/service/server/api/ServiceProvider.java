package com.kush.lib.service.server.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProvider {

    private final Collection<Class<? extends Service>> serviceClasses;
    private final Map<String, Service> services;

    public ServiceProvider(Collection<Class<? extends Service>> serviceClasses) {
        this.serviceClasses = new ArrayList<>(serviceClasses);
        services = new ConcurrentHashMap<>();
    }

    public void initialize(Context context) {
        for (Class<? extends Service> serviceClass : serviceClasses) {
            Service service = instantiateService(serviceClass);
            service.initialize(context);
            services.put(getServiceKey(serviceClass), service);
        }
    }

    private String getServiceKey(Class<? extends Service> serviceClass) {
        return serviceClass.getName();
    }

    private Service instantiateService(Class<? extends Service> serviceClass) {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceInitializationFailedException(e.getMessage(), e);
        }
    }

    public <S extends Service> S getService(Class<S> serviceClass) {
        Service service = services.get(getServiceKey(serviceClass));
        if (service == null) {
            throw new NoSuchServiceExistsException(serviceClass);
        }
        return serviceClass.cast(service);
    }
}
