package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.RemoteServiceInvocationFailedException;
import com.kush.lib.service.client.api.ServiceInvoker;
import com.kush.lib.service.client.helpers.ReflectionBasedServiceInvoker;
import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.NoSuchServiceExistsException;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleServiceInvoker implements ServiceInvoker {

    private final ServiceProvider serviceProvider;

    public SampleServiceInvoker(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Object invoke(String serviceName, String methodName, Object... arguments)
            throws RemoteServiceInvocationFailedException {
        BaseService service;
        try {
            service = serviceProvider.getService(serviceName);
            ServiceInvoker serviceInvoker = new ReflectionBasedServiceInvoker(service);
            return serviceInvoker.invoke(serviceName, methodName, arguments);
        } catch (NoSuchServiceExistsException e) {
            throw new RemoteServiceInvocationFailedException(e);
        }
    }
}
