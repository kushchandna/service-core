package com.kush.lib.service.server.api;

public class ServiceInitializer {

    <S extends BaseService> S initialize(Class<S> serviceClass, Context context)
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
