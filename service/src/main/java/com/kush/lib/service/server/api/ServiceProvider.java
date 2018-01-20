package com.kush.lib.service.server.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceProvider {

    private final Collection<Class<? extends BaseService>> serviceClasses;
    private final Map<String, BaseService> services;

    public ServiceProvider(Collection<Class<? extends BaseService>> serviceClasses) {
        this.serviceClasses = new ArrayList<>(serviceClasses);
        services = new ConcurrentHashMap<>();
    }

    public void initialize(Context context) {
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            BaseService service = instantiateService(serviceClass);
            service.initialize(context);
            services.put(getServiceKey(serviceClass), service);
        }
    }

    private String getServiceKey(Class<? extends BaseService> serviceClass) {
        return serviceClass.getName();
    }

    private BaseService instantiateService(Class<? extends BaseService> serviceClass) {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceInitializationFailedException(e.getMessage(), e);
        }
    }

    public BaseService getService(String serviceName) {
        BaseService service = services.get(serviceName);
        if (service == null) {
            throw new NoSuchServiceExistsException(serviceName);
        }
        return service;
    }
}
