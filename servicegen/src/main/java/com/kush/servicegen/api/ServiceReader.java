package com.kush.servicegen.api;

public interface ServiceReader {

    ServiceInfo readService(Class<?> serviceClass);
}
