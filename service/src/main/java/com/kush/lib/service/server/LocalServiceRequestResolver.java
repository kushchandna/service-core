package com.kush.lib.service.server;

import java.util.Map;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.lib.service.server.authentication.Auth;

class LocalServiceRequestResolver implements ServiceRequestResolver {

    private final Auth authenticator;
    private final Map<ServiceRequestKey, ServiceInvoker> serviceInvokers;

    LocalServiceRequestResolver(Auth authenticator, Map<ServiceRequestKey, ServiceInvoker> serviceInvokers) {
        this.authenticator = authenticator;
        this.serviceInvokers = serviceInvokers;
    }

    @Override
    public Object resolve(ServiceRequest request) throws ServiceRequestFailedException {
        AuthToken token = request.getAuthToken();
        authenticator.login(token);
        try {
            return resolveRequest(request);
        } finally {
            authenticator.logout();
        }
    }

    private Object resolveRequest(ServiceRequest request) throws ServiceRequestFailedException {
        ServiceRequestKey key = new ServiceRequestKey(request.getServiceName(), request.getMethodName());
        ServiceInvoker invoker = serviceInvokers.get(key);
        return invoker.invoke(request.getArgs());
    }
}
