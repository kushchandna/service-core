package com.kush.lib.service.client.internal;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceClientActivationFailedException;

public interface ServiceClientActivator {

    <C extends ServiceClient<?>> C activate(Class<C> serviceClientClass, String serviceName, Executor executor)
            throws ServiceClientActivationFailedException;
}
