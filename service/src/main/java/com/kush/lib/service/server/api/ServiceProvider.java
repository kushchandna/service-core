package com.kush.lib.service.server.api;

public interface ServiceProvider {

    <S extends BaseService> S getService(Class<S> serviceApiClass);
}
