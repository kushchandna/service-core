package com.kush.lib.service.sample.remoting;

import com.kush.lib.service.remoting.api.ServiceInvocationRequest;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public class LocalServiceRequestResolver implements ServiceRequestResolver {

    @Override
    public <T> T resolve(ServiceInvocationRequest request, Class<T> returnType) {
        return null;
    }
}
