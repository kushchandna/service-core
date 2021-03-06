package com.kush.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.service.annotations.ServiceMethod;
import com.kush.service.auth.Auth;
import com.kush.utils.remoting.server.Resolver;

class ServiceInitializer {

    private static final Logger LOGGER = LogManager.getFormatterLogger(ServiceInitializer.class);

    private final Context context;

    private Resolver<ServiceRequest> requestResolver;

    public ServiceInitializer(Context context) {
        this.context = context;
    }

    Resolver<ServiceRequest> initialize(Set<Class<? extends BaseService>> serviceClasses)
            throws ServiceInitializationFailedException {
        Set<Class<? extends BaseService>> servicesToRegister = serviceClasses;
        LOGGER.info("Initializing %d service(s)", servicesToRegister.size());
        Map<ServiceRequestKey, ServiceInvoker> serviceInvokers = new HashMap<>();
        for (Class<? extends BaseService> serviceClass : servicesToRegister) {
            registerServiceInvokers(serviceClass, context, serviceInvokers);
        }
        Auth authenticator = context.getInstance(Auth.class);
        requestResolver = new LocalServiceRequestResolver(authenticator, serviceInvokers);
        return requestResolver;
    }

    Resolver<ServiceRequest> getServiceRequestResolver() {
        return requestResolver;
    }

    private <S extends BaseService> S initializeService(Class<S> serviceClass, Context context)
            throws ServiceInitializationFailedException {
        BaseService service = instantiateService(serviceClass);
        service.initialize(context);
        return serviceClass.cast(service);
    }

    private BaseService instantiateService(Class<? extends BaseService> serviceClass)
            throws ServiceInitializationFailedException {
        try {
            Constructor<? extends BaseService> constructor = serviceClass.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServiceInitializationFailedException(e.getMessage(), e);
        }
    }

    private String getServiceName(Class<? extends BaseService> serviceClass) throws ServiceInitializationFailedException {
        return serviceClass.getName();
    }

    private void registerServiceInvokers(Class<? extends BaseService> serviceClass, Context context,
            Map<ServiceRequestKey, ServiceInvoker> serviceInvokers) throws ServiceInitializationFailedException {
        String serviceName = getServiceName(serviceClass);
        LOGGER.debug("Registering '%s' service", serviceClass.getName());
        BaseService service = initializeService(serviceClass, context);
        Method[] declaredMethods = serviceClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(ServiceMethod.class)) {
                ServiceRequestKey key = new ServiceRequestKey(serviceName, method.getName());
                if (serviceInvokers.containsKey(key)) {
                    throw new ServiceInitializationFailedException("A service method with name '" + key.getMethodName()
                            + "' already exist in service " + key.getServiceName());
                }
                ServiceInvoker invoker = new ServiceInvoker(service, method);
                serviceInvokers.put(key, invoker);
                LOGGER.debug("Added invoker for key '%s'", key);
            }
        }
        LOGGER.info("Registered '%s' service with name '%s'", serviceClass.getName(), serviceName);
    }
}
