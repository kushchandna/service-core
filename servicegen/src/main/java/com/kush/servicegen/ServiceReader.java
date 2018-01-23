package com.kush.servicegen;

public class ServiceReader {

    public ServiceInfo readService(Class<?> serviceClass) {
        return new ServiceInfo(serviceClass);
    }
}
