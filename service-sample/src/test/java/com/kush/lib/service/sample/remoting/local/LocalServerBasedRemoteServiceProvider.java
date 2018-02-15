package com.kush.lib.service.sample.remoting.local;

import com.kush.lib.service.remoting.api.RemoteServiceProvider;
import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.core.ServiceInitializer;
import com.kush.utils.exceptions.ObjectNotFoundException;

public class LocalServerBasedRemoteServiceProvider implements RemoteServiceProvider {

    private final ServiceInitializer serviceProvider;

    public LocalServerBasedRemoteServiceProvider(ServiceInitializer serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public ServiceApi getService(String serviceName) throws ObjectNotFoundException {
        BaseService service = serviceProvider.getService(serviceName);
        String sampleServiceName = getServiceName(SampleHelloService.class);
        if (sampleServiceName.equals(serviceName)) {
            return new SampleLocalHelloServiceApi(SampleHelloService.class.cast(service));
        }
        throw new ObjectNotFoundException("remote service", serviceName);
    }

    private String getServiceName(Class<? extends BaseService> serviceClass) {
        Service service = serviceClass.getAnnotation(Service.class);
        return service.name();
    }
}
