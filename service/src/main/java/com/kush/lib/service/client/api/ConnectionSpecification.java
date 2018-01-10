package com.kush.lib.service.client.api;

import com.kush.lib.service.server.api.BaseService;

public interface ConnectionSpecification {

    <S extends BaseService> S getService(Class<S> serviceClass);
}
