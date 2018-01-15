package com.kush.lib.service.server.utils;

import com.kush.lib.service.remoting.ServiceApi;
import com.kush.lib.service.server.api.annotations.Service;

public class ServiceUtils {

    public static String getServiceName(Class<? extends ServiceApi> serviceClass) {
        if (!serviceClass.isAnnotationPresent(Service.class)) {
            throw new IllegalArgumentException("No @Service annotation found on class " + serviceClass.getName());
        }
        Service serviceAnnotation = serviceClass.getAnnotation(Service.class);
        return serviceAnnotation.name();
    }
}
