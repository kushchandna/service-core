package com.kush.lib.service.server.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;
import com.kush.utils.exceptions.ObjectNotFoundException;

class ServiceInitializer {

    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();
    private final Map<String, BaseService> services = new HashMap<>();

    private final Map<ServiceRequestKey, ServiceInvoker> serviceInvokers = new HashMap<>();

    public void addService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
    }

    public Object handle(ServiceRequest request) throws ServiceRequestFailedException {
        ServiceRequestKey key = new ServiceRequestKey(request.getServiceName(), request.getMethodName());
        ServiceInvoker serviceInvoker = serviceInvokers.get(key);
        return serviceInvoker.invoke(request.getArgs());
    }

    public void initialize(Context context) throws ServiceInitializationFailedException {
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            String serviceName = getServiceName(serviceClass);
            BaseService service = initializeService(serviceClass, context);
            registerHandlers(serviceClass, serviceName, service);
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

    private <S extends BaseService> S initializeService(Class<S> serviceClass, Context context)
            throws ServiceInitializationFailedException {
        BaseService service = instantiateService(serviceClass);
        service.initialize(context);
        return serviceClass.cast(service);
    }

    private BaseService instantiateService(Class<? extends BaseService> serviceClass)
            throws ServiceInitializationFailedException {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceInitializationFailedException(e.getMessage(), e);
        }
    }

    private String getServiceName(Class<? extends BaseService> serviceClass) throws ServiceInitializationFailedException {
        Service service = serviceClass.getAnnotation(Service.class);
        if (service == null) {
            throw new ServiceInitializationFailedException("No @Service annotation found on class " + serviceClass.getName());
        }
        return service.name();
    }

    private void registerHandlers(Class<? extends BaseService> serviceClass, String serviceName, BaseService service)
            throws ServiceInitializationFailedException {
        Method[] declaredMethods = serviceClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(ServiceMethod.class)) {
                ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
                ServiceRequestKey key = new ServiceRequestKey(serviceName, serviceMethod.name());
                if (serviceInvokers.containsKey(key)) {
                    throw new ServiceInitializationFailedException("A service method with name " + key.getMethodName()
                            + " already exist in service " + key.getServiceName());
                }
                ServiceInvoker invoker = new ServiceInvoker(service, method);
                serviceInvokers.put(key, invoker);
            }
        }
    }
}
