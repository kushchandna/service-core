package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.client.api.ServiceInvoker;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleConnectionSpecification implements ConnectionSpecification {

    private final SampleServiceInvoker serviceInvoker;

    public SampleConnectionSpecification(ServiceProvider serviceProvider) {
        serviceInvoker = new SampleServiceInvoker(serviceProvider);
    }

    @Override
    public ServiceInvoker getServiceInvoker() {
        return serviceInvoker;
    }
}
