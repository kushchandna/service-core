package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleConnectionSpecification implements ConnectionSpecification {

    private final ServiceProvider serviceProvider;

    public SampleConnectionSpecification(ApplicationServer server) {
        serviceProvider = server.getServiceProvider();
    }

    @Override
    public <S extends BaseService> S getService(Class<S> serviceClass) {
        return serviceProvider.getService(serviceClass);
    }
}
