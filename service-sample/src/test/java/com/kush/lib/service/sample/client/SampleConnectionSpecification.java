package com.kush.lib.service.sample.client;

import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.remoting.api.RemoteServiceProvider;
import com.kush.lib.service.sample.remoting.local.LocalServerBasedRemoteServiceProvider;
import com.kush.lib.service.server.core.ServiceInitializer;

public class SampleConnectionSpecification implements ConnectionSpecification {

    private final RemoteServiceProvider remoteServiceProvider;

    public SampleConnectionSpecification(ServiceInitializer serviceProvider) {
        remoteServiceProvider = new LocalServerBasedRemoteServiceProvider(serviceProvider);
    }

    @Override
    public RemoteServiceProvider getServiceProvider() {
        return remoteServiceProvider;
    }
}
