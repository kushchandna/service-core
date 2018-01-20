package com.kush.lib.service.sample.client;

import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.remoting.api.RemoteServiceProvider;
import com.kush.lib.service.sample.remoting.local.LocalServerBasedRemoteServiceProvider;
import com.kush.lib.service.server.api.ServiceNameProvider;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleConnectionSpecification implements ConnectionSpecification {

    private final RemoteServiceProvider remoteServiceProvider;

    public SampleConnectionSpecification(ServiceProvider serviceProvider, ServiceNameProvider serviceNameProvider) {
        remoteServiceProvider = new LocalServerBasedRemoteServiceProvider(serviceProvider, serviceNameProvider);
    }

    @Override
    public RemoteServiceProvider getServiceProvider() {
        return remoteServiceProvider;
    }
}
