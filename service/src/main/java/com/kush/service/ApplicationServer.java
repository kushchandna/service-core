package com.kush.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.service.auth.LoginService;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.Resolver;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class ApplicationServer {

    private static final Logger LOGGER = LogManager.getFormatterLogger(ApplicationServer.class);

    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();
    private final ResolutionRequestsReceiver serviceRequestReceiver;

    public ApplicationServer(ResolutionRequestsReceiver serviceRequestReceiver) {
        this.serviceRequestReceiver = serviceRequestReceiver;
    }

    public final void registerService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
        LOGGER.info("Added service '%s'", serviceClass.getName());
    }

    public final void start(Context context) throws StartupFailedException {
        registerService(LoginService.class);
        LOGGER.debug("Starting Application Server");
        ServiceInitializer serviceInitializer = new ServiceInitializer(context);
        Resolver<ServiceRequest> requestResolver = initializeServicesAndGetRequestResolver(serviceInitializer);
        startServiceRequestReceivers(requestResolver);
        LOGGER.info("Application Server Started");
    }

    public final void stop() throws ShutdownFailedException {
        serviceRequestReceiver.stop();
        LOGGER.info("Application Server Stopped");
        System.exit(0);
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
