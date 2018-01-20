package com.kush.lib.service.server.api;

public interface ServiceNameProvider {

    String getServiceName(Class<? extends BaseService> serviceClass);
}
