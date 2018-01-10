package com.kush.lib.service.server.api;

import java.util.HashSet;
import java.util.Set;

import com.kush.lib.service.server.api.annotations.Service;

public class ApplicationServer {

    private final Context context;
    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();

    private ServiceProvider serviceProvider;

    public ApplicationServer(Context context) {
        this.context = context;
    }

    public void registerService(Class<? extends BaseService> serviceClass) {
        if (!serviceClass.isAnnotationPresent(Service.class)) {
            throw new IllegalArgumentException("No @Service annotation found on class " + serviceClass.getName());
        }
        serviceClasses.add(serviceClass);
    }

    public void start() {
        serviceProvider = new ServiceProvider(serviceClasses);
        serviceProvider.initialize(context);
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
