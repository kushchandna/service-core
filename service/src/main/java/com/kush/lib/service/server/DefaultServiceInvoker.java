package com.kush.lib.service.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.kush.lib.service.remoting.api.ServiceRequestFailedException;

class DefaultServiceInvoker implements ServiceInvoker {

    protected final Method method;
    protected final BaseService service;

    DefaultServiceInvoker(BaseService service, Method method) {
        this.service = service;
        this.method = method;
    }

    @Override
    public Object invoke(Object... args) throws ServiceRequestFailedException {
        try {
            return method.invoke(service, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }
}
