package com.kush.serviceclient;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.serviceclient.auth.LoginServiceClient;
import com.kush.serviceclient.auth.SessionManager;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;

public class ApplicationClient {

    private static final Logger LOGGER = LogManager.getFormatterLogger(ApplicationClient.class);

    private final ResolutionConnectionFactory connectionFactory;

    private ServiceClientProvider serviceClientProvider;

    public ApplicationClient(ResolutionConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public final void start() {
        LOGGER.debug("Starting application client");
        SessionManager sessionManager = new SessionManager();
        ServiceClientActivator activator = new ServiceClientActivator(sessionManager, connectionFactory);
        serviceClientProvider = new ServiceClientProvider(activator);
        LOGGER.info("Started application client");
    }

    public final void activateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        requireNonNull(serviceClientProvider, "serviceClientProvider");
        requireNonNull(serviceClientClass, "serviceClientClass");
        requireNonNull(executor, "executor");
        serviceClientProvider.activateServiceClient(serviceClientClass, executor);
    }

    public final void activateLoginServiceClient(Executor executor) throws ServiceClientActivationFailedException {
        activateServiceClient(LoginServiceClient.class, executor);
    }

    public final ServiceClientProvider getServiceClientProvider() {
        return serviceClientProvider;
    }
}
