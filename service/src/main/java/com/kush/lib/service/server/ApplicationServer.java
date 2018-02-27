package com.kush.lib.service.server;

import java.util.HashSet;
import java.util.Set;

import com.kush.lib.service.remoting.ServiceRequestResolver;

public class ApplicationServer {

    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();

    private ServiceInitializer serviceInitializer;

    public void registerService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
    }

    public void start(Context context) throws ServiceInitializationFailedException {
        serviceInitializer = new ServiceInitializer(context);
        serviceInitializer.initialize(serviceClasses);
        serviceClasses.clear();
    }

    protected ServiceRequestResolver getServiceRequestResolver() {
        return serviceInitializer.getServiceRequestResolver();
    }
}
