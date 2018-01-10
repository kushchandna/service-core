package com.kush.lib.service.server.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kush.lib.service.server.api.annotations.Service;

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
        if (!serviceClass.isAnnotationPresent(Service.class)) {
            throw new IllegalArgumentException("No @Service annotation found on class " + serviceClass.getName());
        }
        Service serviceAnnotation = serviceClass.getAnnotation(Service.class);
        return serviceAnnotation.name();
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
