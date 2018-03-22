package com.kush.lib.service.server;

import java.util.HashSet;
import java.util.Set;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;
import com.kush.lib.service.server.authentication.Auth;
import com.kush.lib.service.server.authentication.LoginService;
import com.kush.lib.service.server.authentication.SessionManager;
import com.kush.lib.service.server.authentication.credential.DefaultUserCredentialPersistor;
import com.kush.lib.service.server.authentication.credential.UserCredential;
import com.kush.lib.service.server.authentication.credential.UserCredentialPersistor;
import com.kush.utils.id.SequentialIdGenerator;

public class ApplicationServer {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ApplicationServer.class);

    private final Set<ServiceRequestReceiver> requestReceivers = new HashSet<>();
    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();

    public final void registerServiceRequestReceiver(ServiceRequestReceiver requestReceiver) {
        requestReceivers.add(requestReceiver);
        LOGGER.info("Registered service receiver of type %s", requestReceiver.getClass().getName());
    }

    public final void registerService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
        LOGGER.info("Registered service '%s'", serviceClass.getName());
    }

    public final void start(Context context) throws StartupFailedException {
        LOGGER.info("Starting Application Server");
        enrichContext(context);
        preStartup(context);
        ServiceInitializer serviceInitializer = new ServiceInitializer(context);
        ServiceRequestResolver requestResolver = initializeServicesAndGetRequestResolver(serviceInitializer);
        startServiceRequestReceivers(requestResolver);
        postStartup(context, serviceInitializer.getServiceRequestResolver());
        LOGGER.info("Application Server Started");
    }

    public final void stop() throws ShutdownFailedException {
        for (ServiceRequestReceiver receiver : requestReceivers) {
            receiver.stop();
        }
    }

    protected void preStartup(Context context) {
    }

    protected void postStartup(Context context, ServiceRequestResolver serviceRequestResolver) {
    }

    private void enrichContext(Context context) throws StartupFailedException {
        enrichUserCredentialPersistor(context);
        enrichSessionRelatedInformation(context);
    }

    private void startServiceRequestReceivers(ServiceRequestResolver requestResolver) throws StartupFailedException {
        for (ServiceRequestReceiver receiver : requestReceivers) {
            receiver.start(requestResolver);
        }
    }

    private ServiceRequestResolver initializeServicesAndGetRequestResolver(ServiceInitializer serviceInitializer)
            throws StartupFailedException {
        try {
            return serviceInitializer.initialize(serviceClasses);
        } catch (ServiceInitializationFailedException e) {
            LOGGER.error(e);
            throw new StartupFailedException(e.getMessage(), e);
        }
    }

    private void enrichUserCredentialPersistor(Context context) throws StartupFailedException {
        if (!context.containsKey(UserCredentialPersistor.class)) {
            if (!context.containsPersistor(UserCredential.class)) {
                throw new StartupFailedException("No user credential persistor specified");
            }
            Persistor<UserCredential> delegate = context.getPersistor(UserCredential.class);
            context.addInstance(UserCredentialPersistor.class, new DefaultUserCredentialPersistor(delegate));
        }
    }

    private void enrichSessionRelatedInformation(Context context) {
        if (!context.containsKey(LoginService.KEY_USER_ID_GEN)) {
            context.addInstance(LoginService.KEY_USER_ID_GEN, new SequentialIdGenerator());
        }
        context.addInstance(Auth.class, new Auth());
        context.addInstance(SessionManager.class, new SessionManager());
    }
}
