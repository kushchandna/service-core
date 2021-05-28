package com.kush.serviceclient;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;

import com.kush.serviceclient.auth.SessionManager;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;

class ServiceClientActivator {

    private final SessionManager sessionManager;
    private final ResolutionConnectionFactory connectionFactory;

    public ServiceClientActivator(SessionManager sessionManager, ResolutionConnectionFactory connectionFactory) {
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
            Constructor<C> constructor = serviceClientClass.getConstructor();
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServiceClientActivationFailedException("Failed to activate service client " + serviceClientClass.getName(),
                    e);
        }
    }
}
