package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.api.ServiceRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

class ServerSideServiceRequestResolver implements ServiceRequestResolver {

    private final ServiceInitializer initializer;

    public ServerSideServiceRequestResolver(ServiceInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public <T> T resolve(ServiceRequest request, Class<T> returnType) throws ServiceRequestFailedException {
        Object result = initializer.handle(request);
        return returnType.cast(result);
    }
}
