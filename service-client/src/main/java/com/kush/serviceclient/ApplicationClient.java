package com.kush.serviceclient;

import java.util.concurrent.Executor;

import com.kush.serviceclient.auth.LoginServiceClient;
import com.kush.serviceclient.auth.SessionManager;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;

public class ApplicationClient {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ApplicationClient.class);

    private final ResolutionConnectionFactory connectionFactory;

    private ServiceClientProvider serviceClientProvider;

    public ApplicationClient(ResolutionConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public final void start() {
        LOGGER.info("Starting application client");
        SessionManager sessionManager = new SessionManager();
        ServiceClientActivator activator = new ServiceClientActivator(sessionManager, connectionFactory);
        serviceClientProvider = new ServiceClientProvider(activator);
        LOGGER.info("Started application client");
    }

    public final void activateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        serviceClientProvider.activateServiceClient(serviceClientClass, executor);
    }

    public final void activateLoginServiceClient(Executor executor) throws ServiceClientActivationFailedException {
        activateServiceClient(LoginServiceClient.class, executor);
    }

    public final ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
