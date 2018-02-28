package com.kush.lib.service.server;

import java.util.HashSet;
import java.util.Set;

import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;

public class ApplicationServer {

    private final Set<ServiceRequestReceiver> requestReceivers = new HashSet<>();
    private final Set<Class<? extends BaseService>> serviceClasses = new HashSet<>();

    public final void registerServiceRequestReceiver(ServiceRequestReceiver requestReceiver) {
        requestReceivers.add(requestReceiver);
    }

    public final void registerService(Class<? extends BaseService> serviceClass) {
        serviceClasses.add(serviceClass);
    }

    public final void start(Context context) throws StartupFailedException {
        preStartup(context);
        ServiceInitializer serviceInitializer = new ServiceInitializer(context);
        ServiceRequestResolver requestResolver = initializeServicesAndGetRequestResolver(serviceInitializer);
        startServiceRequestReceivers(requestResolver);
        postStartup(context, serviceInitializer.getServiceRequestResolver());
    }

    protected void preStartup(Context context) {
    }

    protected void postStartup(Context context, ServiceRequestResolver serviceRequestResolver) {
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
            throw new StartupFailedException(e.getMessage(), e);
        }
    }
}
