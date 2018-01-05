package com.kush.lib.service.sample.server;

import java.util.Arrays;
import java.util.Collection;

import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.Service;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleApplicationServer {

    private ServiceProvider serviceProvider;

    public void init() {
        Context context = new Context();
        serviceProvider = new ServiceProvider(context);
        Collection<Class<? extends Service>> serviceClasses = Arrays.asList(
                SampleService.class);
        serviceProvider.initialize(serviceClasses);
    }

    public <S extends Service> S getService(Class<S> serviceClass) {
        return serviceProvider.getService(serviceClass);
    }
}
