package com.kush.lib.service.server;

import java.util.Map;

import com.kush.lib.service.remoting.api.ServiceRequest;

class DefaultServiceInvokerProvider implements ServiceInvokerProvider {

    private final Map<ServiceRequestKey, ServiceInvoker> keyVsInvokers;

    public DefaultServiceInvokerProvider(Map<ServiceRequestKey, ServiceInvoker> keyVsInvokers) {
        this.keyVsInvokers = keyVsInvokers;
    }

    @Override
    public ServiceInvoker getInvoker(ServiceRequest<?> request) {
        ServiceRequestKey key = new ServiceRequestKey(request.getServiceName(), request.getMethodName());
        return keyVsInvokers.get(key);
    }
}
