package com.kush.servicegen;

import static java.util.Collections.unmodifiableList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;

public class ServiceInfo {

    private final Class<?> serviceClass;

    public ServiceInfo(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceId() {
        Service service = serviceClass.getAnnotation(Service.class);
        return service.name();
    }

    public String getName() {
        return serviceClass.getSimpleName();
    }

    public List<ServiceMethodInfo> getServiceMethods() {
        Method[] declaredMethods = serviceClass.getDeclaredMethods();
        List<ServiceMethodInfo> serviceMethodInfos = new ArrayList<>();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(ServiceMethod.class)) {
                serviceMethodInfos.add(new ServiceMethodInfo(method));
            }
        }
        sortMethodsWithName(serviceMethodInfos);
        return unmodifiableList(serviceMethodInfos);
    }

    private void sortMethodsWithName(List<ServiceMethodInfo> serviceMethodInfos) {
        serviceMethodInfos.sort(new Comparator<ServiceMethodInfo>() {

            @Override
            public int compare(ServiceMethodInfo o1, ServiceMethodInfo o2) {
                return o1.getMethodName().compareTo(o2.getMethodName());
            }
        });
    }
}
