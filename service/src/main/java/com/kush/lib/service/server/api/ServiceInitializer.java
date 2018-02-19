package com.kush.lib.service.server.api;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;

class ServiceInitializer {

    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();

    private ServiceRequestResolver serviceRequestResolver;

    public void addService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
    }

    public void initialize(Context context) throws ServiceInitializationFailedException {
        Map<ServiceRequestKey, ServiceInvoker> serviceInvokers = new HashMap<>();
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            registerServiceInvokers(serviceClass, context, serviceInvokers);
        }
        serviceRequestResolver = new ServerSideServiceRequestResolver(serviceInvokers);
    }

    public ServiceRequestResolver getServiceRequestResolver() {
        return serviceRequestResolver;
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

    private void registerServiceInvokers(Class<? extends BaseService> serviceClass, Context context,
            Map<ServiceRequestKey, ServiceInvoker> serviceInvokers) throws ServiceInitializationFailedException {
        String serviceName = getServiceName(serviceClass);
        BaseService service = initializeService(serviceClass, context);
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
