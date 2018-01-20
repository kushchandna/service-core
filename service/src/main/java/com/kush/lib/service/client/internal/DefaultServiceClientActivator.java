package com.kush.lib.service.client.internal;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceClientActivationFailedException;
import com.kush.lib.service.remoting.api.RemoteServiceProvider;
import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.utils.exceptions.ObjectNotFoundException;

public class DefaultServiceClientActivator implements ServiceClientActivator {

    private final RemoteServiceProvider serviceApiProvider;

    public DefaultServiceClientActivator(RemoteServiceProvider serviceApiProvider) {
        this.serviceApiProvider = serviceApiProvider;
    }

    @Override
    public <C extends ServiceClient<?>> C activate(Class<C> serviceClientClass, String serviceName, Executor executor)
            throws ServiceClientActivationFailedException {
        try {
            C serviceClient = instantiateServiceClient(serviceClientClass);
            ServiceApi serviceApi = serviceApiProvider.getService(serviceName);
            serviceClient.activate(executor, serviceApi);
            return serviceClient;
        } catch (ReflectiveOperationException | ObjectNotFoundException e) {
            throw new ServiceClientActivationFailedException("Failed to activate service client " + serviceClientClass.getName(),
                    e);
        }
    }

    private <C extends ServiceClient<?>> C instantiateServiceClient(Class<C> serviceClientClass)
            throws ReflectiveOperationException {
        return serviceClientClass.newInstance();
    }
}
