package com.kush.lib.service.server.internal;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ServiceInitializationFailedException;
import com.kush.lib.service.server.api.ServiceInitializer;

public class DefaultServiceInitializer implements ServiceInitializer {

    @Override
    public <S extends BaseService> S initialize(Class<S> serviceClass, Context context)
            throws ServiceInitializationFailedException {
        BaseService service = instantiateService(serviceClass);
        service.initialize(context);
        return serviceClass.cast(service);
    }

    private BaseService instantiateService(Class<? extends BaseService> serviceClass)
            throws ServiceInitializationFailedException {
        try {
            return serviceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceInitializationFailedException(e.getMessage(), e);
        }
    }
}
