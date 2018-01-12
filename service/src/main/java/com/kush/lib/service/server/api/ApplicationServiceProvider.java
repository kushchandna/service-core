package com.kush.lib.service.server.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kush.lib.service.remoting.ServiceApi;
import com.kush.lib.service.remoting.ServiceProvider;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.logger.Logger;
import com.kush.logger.LoggerFactory;

public class ApplicationServiceProvider implements ServiceProvider {

    private static final Logger LOGGER = LoggerFactory.INSTANCE.getLogger(ApplicationServiceProvider.class);

    private final Collection<Class<? extends BaseService>> serviceClasses;
    private final Map<String, BaseService> services;

    public ApplicationServiceProvider(Collection<Class<? extends BaseService>> serviceClasses) {
        this.serviceClasses = new ArrayList<>(serviceClasses);
        services = new ConcurrentHashMap<>();
    }

    public void initialize(Context context) {
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Creating instance for service class %s", serviceClass.getName());
            }
            BaseService service = instantiateService(serviceClass);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Initializing created instance of service %s", serviceClass.getName());
            }
            service.initialize(context);
            String serviceKey = getServiceKey(serviceClass);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Service %s initialized with key %s", serviceClass.getName(), serviceKey);
            }
            services.put(serviceKey, service);
        }
    }

    private String getServiceKey(Class<? extends ServiceApi> serviceClass) {
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

    @Override
    public <S extends ServiceApi> S getService(Class<S> serviceApiClass) {
        BaseService service = services.get(getServiceKey(serviceApiClass));
        if (service == null) {
            throw new NoSuchServiceExistsException(serviceApiClass);
        }
        return serviceApiClass.cast(service);
    }
}
