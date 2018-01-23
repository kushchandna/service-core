package com.kush.servicegen;

import static java.lang.reflect.Modifier.isPublic;
import static java.util.Collections.unmodifiableList;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ServiceInfo {

    private final Class<?> serviceClass;

    public ServiceInfo(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getName() {
        return serviceClass.getSimpleName();
    }

    public List<MethodInfo> getServiceMethods() {
        Method[] declaredMethods = serviceClass.getDeclaredMethods();
        List<MethodInfo> serviceMethodInfos = new ArrayList<>();
        for (Method method : declaredMethods) {
            if (isPublic(method.getModifiers())) {
                serviceMethodInfos.add(new MethodInfo(method));
            }
        }
        sortMethodsWithName(serviceMethodInfos);
        return unmodifiableList(serviceMethodInfos);
    }

    private void sortMethodsWithName(List<MethodInfo> serviceMethodInfos) {
        serviceMethodInfos.sort(new Comparator<MethodInfo>() {

            @Override
            public int compare(MethodInfo o1, MethodInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
}
