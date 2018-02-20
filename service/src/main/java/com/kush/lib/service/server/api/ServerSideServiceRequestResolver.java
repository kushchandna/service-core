package com.kush.lib.service.server.api;

import java.util.Map;

import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

class ServerSideServiceRequestResolver implements ServiceRequestResolver {

    private final Map<ServiceRequestKey, ServiceInvoker> serviceInvokers;

    public ServerSideServiceRequestResolver(Map<ServiceRequestKey, ServiceInvoker> serviceInvokers) {
        this.serviceInvokers = serviceInvokers;
    }

    @Override
    public <T> T resolve(ServiceRequest<T> request) throws ServiceRequestFailedException {
        ServiceRequestKey key = new ServiceRequestKey(request.getServiceName(), request.getMethodName());
        ServiceInvoker serviceInvoker = serviceInvokers.get(key);
        Object result = serviceInvoker.invoke(request.getArgs());
        return request.getReturnType().cast(result);
    }
}
