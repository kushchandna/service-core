package com.kush.lib.service.server.api;

import static com.kush.lib.service.server.utils.ServiceUtils.getServiceName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            String serviceKey = getServiceName(serviceClass);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Service %s initialized with key %s", serviceClass.getName(), serviceKey);
            }
            services.put(serviceKey, service);
        }
    }

    private BaseService instantiateService(Class<? extends BaseService> serviceClass) {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceInitializationFailedException(e.getMessage(), e);
        }
    }

    @Override
    public <S extends BaseService> S getService(Class<S> serviceClass) {
        BaseService service = services.get(getServiceName(serviceClass));
        if (service == null) {
            throw new NoSuchServiceExistsException(serviceClass);
        }
        return serviceClass.cast(service);
    }
}
