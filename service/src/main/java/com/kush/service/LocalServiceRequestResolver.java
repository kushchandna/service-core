package com.kush.service;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.auth.AuthToken;
import com.kush.service.auth.Auth;
import com.kush.utils.remoting.server.Resolver;

class LocalServiceRequestResolver implements Resolver<ServiceRequest> {

    private static final Logger LOGGER = LogManager.getFormatterLogger(LocalServiceRequestResolver.class);

    private final Auth authenticator;
    private final Map<ServiceRequestKey, ServiceInvoker> serviceInvokers;

    LocalServiceRequestResolver(Auth authenticator, Map<ServiceRequestKey, ServiceInvoker> serviceInvokers) {
        this.authenticator = authenticator;
        this.serviceInvokers = serviceInvokers;
    }

    @Override
    public Object resolve(ServiceRequest request) throws ServiceRequestFailedException {
        LOGGER.debug("Received service request %s", request);
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
        if (invoker == null) {
            String msg = String.format("No service invoker registered for key %s", key);
            throw new ServiceRequestFailedException(msg);
        }
        return invoker.invoke(request.getArgs());
    }
}
