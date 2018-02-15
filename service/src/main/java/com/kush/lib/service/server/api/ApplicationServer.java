package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;
import com.kush.lib.service.server.core.ServerSideServiceRequestResolver;
import com.kush.lib.service.server.core.ServiceInitializer;

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

    public ServiceInitializer getServiceProvider() {
        return serviceInitializer;
    }

    public ServiceRequestResolver getServiceRequestResolver() {
        return new ServerSideServiceRequestResolver(serviceInitializer);
    }
}
