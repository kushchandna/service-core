package com.kush.lib.service.server;

import com.kush.lib.service.remoting.ServiceRequestResolver;

public class ApplicationServer {

    private final ServiceInitializer serviceInitializer;

    public ApplicationServer() {
        serviceInitializer = new ServiceInitializer();
    }

    public void registerService(Class<? extends BaseService> serviceClass) {
        serviceInitializer.addService(serviceClass);
    }

    public void start(Context context) throws ServiceInitializationFailedException {
        serviceInitializer.initialize(context);
    }

    ServiceRequestResolver getServiceRequestResolver() {
        return serviceInitializer.getServiceRequestResolver();
    }
}
