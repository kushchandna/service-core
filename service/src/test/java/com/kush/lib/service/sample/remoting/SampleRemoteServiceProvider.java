package com.kush.lib.service.sample.remoting;

import com.kush.lib.service.remoting.RemoteServiceProvider;
import com.kush.lib.service.remoting.ServiceApi;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleRemoteServiceProvider implements RemoteServiceProvider {

    private final ServiceProvider serviceProvider;

    public SampleRemoteServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public ServiceApi getRemoteService(String serviceName) {
        return null;
    }
}
