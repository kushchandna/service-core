package com.kush.lib.service.server;

import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

class DefaultServiceRequestResolver implements ServiceRequestResolver {

    private final ServiceInvokerProvider serviceInvokerProvider;

    public DefaultServiceRequestResolver(ServiceInvokerProvider serviceInvokerProvider) {
        this.serviceInvokerProvider = serviceInvokerProvider;
    }

    @Override
    public <T> T resolve(ServiceRequest<T> request) throws ServiceRequestFailedException {
        ServiceInvoker invoker = serviceInvokerProvider.getInvoker(request);
        Object result = invoker.invoke(request.getArgs());
        return request.getReturnType().cast(result);
    }
}
