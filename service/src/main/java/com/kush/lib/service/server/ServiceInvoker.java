package com.kush.lib.service.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.kush.lib.service.remoting.ServiceRequestFailedException;

class ServiceInvoker {

    protected final Method method;
    protected final BaseService service;

    ServiceInvoker(BaseService service, Method method) {
        this.service = service;
        this.method = method;
    }

    Object invoke(Object... args) throws ServiceRequestFailedException {
        try {
            return method.invoke(service, args);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new ServiceRequestFailedException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new ServiceRequestFailedException(cause.getMessage(), cause);
        }
    }
}
