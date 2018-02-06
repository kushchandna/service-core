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
        ServiceApi serviceApi = getServiceApi(serviceName);
        C serviceClient = instantiateServiceClient(serviceClientClass);
        serviceClient.activate(executor, serviceApi);
        return serviceClient;
    }

    private <C extends ServiceClient<?>> ServiceApi getServiceApi(String serviceName)
            throws ServiceClientActivationFailedException {
        try {
            return serviceApiProvider.getService(serviceName);
        } catch (ObjectNotFoundException e) {
            throw new ServiceClientActivationFailedException("Failed to find service with name " + serviceName, e);
        }
    }

    private <C extends ServiceClient<?>> C instantiateServiceClient(Class<C> serviceClientClass)
            throws ServiceClientActivationFailedException {
        try {
            return serviceClientClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ServiceClientActivationFailedException("Failed to activate service client " + serviceClientClass.getName(),
                    e);
        }
    }
}
