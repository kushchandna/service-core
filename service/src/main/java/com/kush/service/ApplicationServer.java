package com.kush.service;

import java.util.HashSet;
import java.util.Set;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.Resolver;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class ApplicationServer {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ApplicationServer.class);

    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();
    private final ResolutionRequestsReceiver serviceRequestReceiver;

    public ApplicationServer(ResolutionRequestsReceiver serviceRequestReceiver) {
        this.serviceRequestReceiver = serviceRequestReceiver;
    }

    public final void registerService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
        LOGGER.info("Registered service '%s'", serviceClass.getName());
    }

    public final void start(Context context) throws StartupFailedException {
        LOGGER.info("Starting Application Server");
        ServiceInitializer serviceInitializer = new ServiceInitializer(context);
        Resolver<ServiceRequest> requestResolver = initializeServicesAndGetRequestResolver(serviceInitializer);
        startServiceRequestReceivers(requestResolver);
        LOGGER.info("Application Server Started");
    }

    public final void stop() throws ShutdownFailedException {
        serviceRequestReceiver.stop();
    }

    private void startServiceRequestReceivers(Resolver<ServiceRequest> requestResolver) throws StartupFailedException {
        serviceRequestReceiver.addResolver(ServiceRequest.class, requestResolver);
        serviceRequestReceiver.start();
    }

    private Resolver<ServiceRequest> initializeServicesAndGetRequestResolver(ServiceInitializer serviceInitializer)
            throws StartupFailedException {
        try {
            return serviceInitializer.initialize(serviceClasses);
        } catch (ServiceInitializationFailedException e) {
            LOGGER.error(e);
            throw new StartupFailedException(e.getMessage(), e);
        }
    }
}
