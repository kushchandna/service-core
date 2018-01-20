package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.ServiceNameProvider;

public class SampleServiceNameProvider implements ServiceNameProvider {

    @Override
    public String getServiceName(Class<? extends BaseService> serviceClass) {
        if (SampleHelloService.class.getName().equals(serviceClass)) {
            return "Sample Service";
        }
        throw new IllegalArgumentException("No service defined for class " + serviceClass.getName());
    }
}
