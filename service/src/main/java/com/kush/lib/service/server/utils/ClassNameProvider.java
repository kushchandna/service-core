package com.kush.lib.service.server.utils;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.ServiceNameProvider;

public class ClassNameProvider implements ServiceNameProvider {

    @Override
    public String getServiceName(Class<? extends BaseService> serviceClass) {
        return serviceClass.getName();
    }
}
