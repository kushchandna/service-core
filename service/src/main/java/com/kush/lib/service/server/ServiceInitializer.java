package com.kush.lib.service.server;

import static com.kush.lib.service.server.authentication.Auth.DEFAULT;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.LoginService;

class ServiceInitializer {

    private final Context context;

    private ServiceRequestResolver requestResolver;

    public ServiceInitializer(Context context) {
        this.context = context;
    }

    ServiceRequestResolver initialize(Set<Class<? extends BaseService>> serviceClasses)
            throws ServiceInitializationFailedException {
        Map<ServiceRequestKey, ServiceInvoker> serviceInvokers = new HashMap<>();
        for (Class<? extends BaseService> serviceClass : serviceClasses) {
            registerServiceInvokers(serviceClass, context, serviceInvokers);
        }
        initializeLoginService(serviceInvokers);
        Auth authenticator = context.getInstance(Auth.class, DEFAULT);
        requestResolver = new LocalServiceRequestResolver(authenticator, serviceInvokers);
        return requestResolver;
    }

    private void initializeLoginService(Map<ServiceRequestKey, ServiceInvoker> serviceInvokers)
            throws ServiceInitializationFailedException {
        registerServiceInvokers(LoginService.class, context, serviceInvokers);
    }

    ServiceRequestResolver getServiceRequestResolver() {
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
