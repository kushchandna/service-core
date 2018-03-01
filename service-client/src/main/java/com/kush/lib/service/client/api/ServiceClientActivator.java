package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.session.SessionManager;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;

class ServiceClientActivator {

    private final SessionManager sessionManager;
    private final ServiceConnectionFactory connectionFactory;

    public ServiceClientActivator(SessionManager sessionManager, ServiceConnectionFactory connectionFactory) {
        this.sessionManager = sessionManager;
        this.connectionFactory = connectionFactory;
    }

    public <C extends ServiceClient> C activate(Class<C> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        C serviceClient = instantiateServiceClient(serviceClientClass);
        serviceClient.activate(executor, connectionFactory, sessionManager);
        return serviceClient;
    }

    private <C extends ServiceClient> C instantiateServiceClient(Class<C> serviceClientClass)
            throws ServiceClientActivationFailedException {
        try {
            return serviceClientClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServiceClientActivationFailedException("Failed to activate service client " + serviceClientClass.getName(),
                    e);
        }
    }
}
