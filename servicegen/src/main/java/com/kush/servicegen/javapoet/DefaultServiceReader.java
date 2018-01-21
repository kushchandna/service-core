package com.kush.servicegen.javapoet;

import com.kush.servicegen.api.ServiceInfo;
import com.kush.servicegen.api.ServiceReader;

class DefaultServiceReader implements ServiceReader {

    @Override
    public ServiceInfo readService(Class<?> serviceClass) {
        return new DefaultServiceInfo(serviceClass);
    }
}
