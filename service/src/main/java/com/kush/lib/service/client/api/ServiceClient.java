package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

public abstract class ServiceClient {

    private final Responder responder;
    private final ServiceInvoker serviceInvoker;
    private final String serviceName;

    public ServiceClient(Executor executor, ServiceInvoker serviceInvoker, String serviceName) {
        this.serviceInvoker = serviceInvoker;
        this.serviceName = serviceName;
        responder = new Responder(executor);
    }

    protected final <T> Response<T> invoke(Class<T> returnType, String methodName, Object... arguments) {
        ServiceTask<T> serviceTask = createServiceTask(returnType, methodName, arguments);
        return responder.invoke(serviceTask);
    }

    private <T> ServiceTask<T> createServiceTask(Class<T> returnType, String methodName, Object... arguments) {
        return new ServiceTask<T>() {

            @Override
            public T execute() throws ServiceFailedException {
                return returnType.cast(serviceInvoker.invoke(serviceName, methodName, arguments));
            }
        };
    }
}
