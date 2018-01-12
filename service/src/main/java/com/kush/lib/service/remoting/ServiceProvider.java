package com.kush.lib.service.remoting;

public interface ServiceProvider {

    <S extends ServiceApi> S getService(Class<S> serviceApiClass);
}
