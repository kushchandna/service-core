package com.kush.lib.service.server.internal;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ServiceInitializationFailedException;

public interface ServiceInitializer {

    <S extends BaseService> S initialize(Class<S> serviceClass, Context context) throws ServiceInitializationFailedException;
}
