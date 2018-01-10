package com.kush.lib.service.server.api;

import java.util.HashSet;
import java.util.Set;

public class ApplicationServer {

    private final Context context;
    private final Set<Class<? extends Service>> serviceClasses = new HashSet<>();

    private ServiceProvider serviceProvider;

    public ApplicationServer(Context context) {
        this.context = context;
    }

    public void registerService(Class<? extends Service> serviceClass) {
        serviceClasses.add(serviceClass);
    }

    public void start() {
        serviceProvider = new ServiceProvider(serviceClasses);
        serviceProvider.initialize(context);
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
