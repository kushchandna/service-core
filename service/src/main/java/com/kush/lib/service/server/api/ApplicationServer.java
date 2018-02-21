package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public class ApplicationServer {

    private final ServiceRequestResolverFactory resolverFactory;
    private final ServiceInitializer serviceInitializer;

    private ServiceRequestResolver requestResolver;

    public ApplicationServer() {
        this(ServiceRequestResolverFactory.DEFAULT);
    }

    public ApplicationServer(ServiceRequestResolverFactory resolverFactory) {
        this.resolverFactory = resolverFactory;
        serviceInitializer = new ServiceInitializer();
    }

    public void registerService(Class<? extends BaseService> serviceClass) {
        serviceInitializer.addService(serviceClass);
    }

    public void start(Context context) throws ServiceInitializationFailedException {
        ServiceInvokerProvider invokerProvider = serviceInitializer.initialize(context);
        requestResolver = resolverFactory.create(invokerProvider);
    }

    ServiceRequestResolver getServiceRequestResolver() {
        return requestResolver;
    }
}
