package com.kush.lib.service.client.helpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.kush.lib.service.client.api.RemoteServiceInvocationFailedException;
import com.kush.lib.service.client.api.ServiceInvoker;
import com.kush.lib.service.server.api.BaseService;

public class ReflectionBasedServiceInvoker implements ServiceInvoker {

    private final BaseService service;

    public ReflectionBasedServiceInvoker(BaseService service) {
        this.service = service;
    }

    @Override
    public Object invoke(String serviceName, String methodName, Object... arguments)
            throws RemoteServiceInvocationFailedException {
        Class<? extends BaseService> serviceClass = service.getClass();
        List<Class<?>> argClasses = new ArrayList<>();
        for (Object arg : arguments) {
            Class<? extends Object> argClass = arg.getClass();
            argClasses.add(argClass);
        }
        try {
            Method method = serviceClass.getMethod(methodName, argClasses.toArray(new Class<?>[arguments.length]));
            return method.invoke(service, arguments);
        } catch (ReflectiveOperationException e) {
            throw new RemoteServiceInvocationFailedException(e);
        }
    }
}
