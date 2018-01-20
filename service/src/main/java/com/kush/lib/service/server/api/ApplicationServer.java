package com.kush.lib.service.server.api;

public class ApplicationServer {

    private final ServiceProvider serviceProvider;

    public ApplicationServer() {
        ServiceInitializer initializer = new ServiceInitializer();
        serviceProvider = new ServiceProvider(initializer);
    }

    public void registerService(Class<? extends BaseService> serviceClass) {
        serviceProvider.addService(serviceClass);
    }

    public void start(Context context) throws ServiceInitializationFailedException {
        serviceProvider.initialize(context);
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
