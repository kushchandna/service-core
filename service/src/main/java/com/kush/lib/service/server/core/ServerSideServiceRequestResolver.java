package com.kush.lib.service.server.core;

import com.kush.lib.service.remoting.api.ServiceInvocationRequest;
import com.kush.lib.service.remoting.api.ServiceRequestFailedException;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public class ServerSideServiceRequestResolver implements ServiceRequestResolver {

    private final ServiceInitializer initializer;

    public ServerSideServiceRequestResolver(ServiceInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public <T> T resolve(ServiceInvocationRequest request, Class<T> returnType) throws ServiceRequestFailedException {
        Object result = initializer.handle(request);
        return returnType.cast(result);
    }
}
