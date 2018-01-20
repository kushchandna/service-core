package com.kush.lib.service.server.api;

public interface ServiceInitializer {

    <S extends BaseService> S initialize(Class<S> serviceClass, Context context) throws ServiceInitializationFailedException;
}
