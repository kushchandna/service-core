package com.kush.lib.service.client.api;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.session.LoginServiceClient;
import com.kush.lib.service.client.api.session.SessionManager;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;

public class ApplicationClient {

    private ServiceClientProvider serviceClientProvider;

    public void start(ServiceConnectionFactory connectionFactory) {
        SessionManager sessionManager = new SessionManager();
        ServiceClientActivator activator = new ServiceClientActivator(sessionManager, connectionFactory);
        serviceClientProvider = new ServiceClientProvider(activator);
    }

    public void activateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        serviceClientProvider.activateServiceClient(serviceClientClass, executor);
    }

    public void activateLoginServiceClient(Executor executor) throws ServiceClientActivationFailedException {
        activateServiceClient(LoginServiceClient.class, executor);
    }

    public ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
