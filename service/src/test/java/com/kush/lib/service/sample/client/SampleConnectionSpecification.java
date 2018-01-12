package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceProvider;

public class SampleConnectionSpecification implements ConnectionSpecification {

    private final ServiceProvider serviceProvider;

    public SampleConnectionSpecification(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public ServiceProvider getRemoteServiceProvider() {
        return serviceProvider;
    }
}
