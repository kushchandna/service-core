package com.kush.lib.service.server.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kush.lib.service.server.utils.ClassNameProvider;

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
        ServiceNameProvider nameProvider = getServiceNameProvider(context);
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            BaseService service = initializer.initialize(serviceClass, context);
            String serviceName = nameProvider.getServiceName(serviceClass);
            services.put(serviceName, service);
        }
    }

    public BaseService getService(String serviceName) throws NoSuchServiceExistsException {
        BaseService service = services.get(serviceName);
        if (service == null) {
            throw new NoSuchServiceExistsException(serviceName);
        }
        return service;
    }

    private ServiceNameProvider getServiceNameProvider(Context context) {
        ServiceNameProvider nameProvider = context.getInstance(ServiceNameProvider.class);
        if (nameProvider == null) {
            nameProvider = new ClassNameProvider();
        }
        return nameProvider;
    }
}
