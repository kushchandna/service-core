package com.kush.lib.service.client.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.kush.utils.exceptions.ObjectNotFoundException;

public class ServiceClientProvider {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ServiceClientProvider.class);

    private final Map<Class<? extends ServiceClient>, ServiceClient> serviceClients = new HashMap<>();

    private final ServiceClientActivator activator;

    public ServiceClientProvider(ServiceClientActivator activator) {
        this.activator = activator;
    }

    void activateServiceClient(Class<? extends ServiceClient> serviceClientClass, Executor executor)
            throws ServiceClientActivationFailedException {
        LOGGER.info("Activating service client %s", serviceClientClass.getName());
        ServiceClient serviceClient = activator.activate(serviceClientClass, executor);
        serviceClients.put(serviceClientClass, serviceClient);
        LOGGER.info("Activated service client %s", serviceClientClass.getName());
    }

    public <S extends ServiceClient> S getServiceClient(Class<S> serviceClientClass) throws ObjectNotFoundException {
        ServiceClient serviceClient = serviceClients.get(serviceClientClass);
        if (serviceClient == null) {
            throw new ObjectNotFoundException("service client", serviceClientClass);
        }
        return serviceClientClass.cast(serviceClient);
    }
}
