package com.kush.serviceclient;

import java.util.concurrent.Executor;

import com.kush.serviceclient.auth.LoginServiceClient;
import com.kush.serviceclient.auth.SessionManager;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;

public class ApplicationClient {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ApplicationClient.class);

    private ServiceClientProvider serviceClientProvider;

    public void start(ResolutionConnectionFactory connectionFactory) {
        LOGGER.info("Starting application client");
        SessionManager sessionManager = new SessionManager();
        ServiceClientActivator activator = new ServiceClientActivator(sessionManager, connectionFactory);
        serviceClientProvider = new ServiceClientProvider(activator);
        LOGGER.info("Started application client");
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
