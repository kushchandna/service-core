package com.kush.lib.service.sample.remoting.local;

import com.kush.lib.service.remoting.api.RemoteServiceProvider;
import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.NoSuchServiceExistsException;
import com.kush.lib.service.server.api.ServiceNameProvider;
import com.kush.lib.service.server.api.ServiceProvider;

public class LocalServerBasedRemoteServiceProvider implements RemoteServiceProvider {

    private final ServiceProvider serviceProvider;
    private final ServiceNameProvider serviceNameProvider;

    public LocalServerBasedRemoteServiceProvider(ServiceProvider serviceProvider, ServiceNameProvider serviceNameProvider) {
        this.serviceProvider = serviceProvider;
        this.serviceNameProvider = serviceNameProvider;
    }

    @Override
    public ServiceApi getService(String serviceName) throws NoSuchServiceExistsException {
        BaseService service = serviceProvider.getService(serviceName);
        String sampleServiceName = serviceNameProvider.getServiceName(SampleHelloService.class);
        if (sampleServiceName.equals(serviceName)) {
            return new SampleLocalHelloServiceApi(SampleHelloService.class.cast(service));
        }
        throw new NoSuchServiceExistsException(serviceName);
    }
}
