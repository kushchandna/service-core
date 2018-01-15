package com.kush.lib.service.server.api;

import java.util.HashSet;
import java.util.Set;

import com.kush.lib.service.server.api.annotations.Service;
import com.kush.logger.Logger;
import com.kush.logger.LoggerFactory;

public class ApplicationServer {

    private static final Logger LOGGER = LoggerFactory.INSTANCE.getLogger(ApplicationServer.class);

    private final Context context;
    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();

    private ApplicationServiceProvider serviceProvider;

    public ApplicationServer(Context context) {
        this.context = context;
    }

    public void registerService(Class<? extends BaseService> serviceClass) {
        if (!serviceClass.isAnnotationPresent(Service.class)) {
            String errorMessage = "No @Service annotation found on class " + serviceClass.getName();
            LOGGER.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (!serviceClasses.contains(serviceClass)) {
            serviceClasses.add(serviceClass);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Registered service %s", serviceClass.getName());
            }
        } else {
            LOGGER.warn("Service with name %s is already registered. Ignoring...", serviceClass.getName());
        }
    }

    public Set<Class<? extends BaseService>> getRegisteredServices() {
        return serviceClasses;
    }

    public void start() {
        serviceProvider = new ApplicationServiceProvider(serviceClasses);
        LOGGER.info("Initializing service provider...");
        serviceProvider.initialize(context);
        LOGGER.info("Initialized service provider with %d services", serviceClasses.size());
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
