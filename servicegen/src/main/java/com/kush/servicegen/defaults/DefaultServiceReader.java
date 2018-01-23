package com.kush.servicegen.defaults;

import com.kush.servicegen.api.ServiceInfo;
import com.kush.servicegen.api.ServiceReader;

public class DefaultServiceReader implements ServiceReader {

    @Override
    public ServiceInfo readService(Class<?> serviceClass) {
        return new DefaultServiceInfo(serviceClass);
    }
}
