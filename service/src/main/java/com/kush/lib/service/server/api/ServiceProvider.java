package com.kush.lib.service.server.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kush.utils.exceptions.ObjectNotFoundException;

public class ServiceProvider {

    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();
    private final Map<String, BaseService> services = new HashMap<>();

    private final ServiceInitializer initializer;

    public ServiceProvider(ServiceInitializer initializer) {
        this.initializer = initializer;
    }

    void addService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
    }

    void initialize(Context context) throws ServiceInitializationFailedException {
        ServiceNameProvider nameProvider = context.getInstance(ServiceNameProvider.class);
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            BaseService service = initializer.initialize(serviceClass, context);
            String serviceName = nameProvider.getServiceName(serviceClass);
            services.put(serviceName, service);
        }
    }

    public BaseService getService(String serviceName) throws ObjectNotFoundException {
        BaseService service = services.get(serviceName);
        if (service == null) {
            throw new ObjectNotFoundException("service", serviceName);
        }
        return service;
    }
}
