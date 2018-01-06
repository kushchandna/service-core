package com.kush.lib.service.sample.server;

import static java.util.Arrays.asList;

import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.Service;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleApplicationServer {

    private ServiceProvider serviceProvider;

    public void init() {
        SampleGreetingProvider greetingProvider = new SampleGreetingProvider();
        Context context = ContextBuilder.create()
            .withInstance(SampleGreetingProvider.class, greetingProvider)
            .build();
        serviceProvider = new ServiceProvider(asList(SampleService.class));
        serviceProvider.initialize(context);
    }

    public <S extends Service> S getService(Class<S> serviceClass) {
        return serviceProvider.getService(serviceClass);
    }
}
