package com.kush.servicegen;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.kush.lib.service.server.annotations.ServiceMethod;

public class ServiceMethodInfo {

    private final Method method;

    public ServiceMethodInfo(Method serviceMethod) {
        method = serviceMethod;
    }

    public String getServiceMethodId() {
        ServiceMethod serviceMethod = method.getAnnotation(ServiceMethod.class);
        return serviceMethod.name();
    }

    public String getMethodName() {
        return method.getName();
    }

    public List<ParameterInfo> getParameters() {
        List<ParameterInfo> paramInfos = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            ParameterInfo paramInfo = new ParameterInfo(parameter);
            paramInfos.add(paramInfo);
        }
        return paramInfos;
    }

    public Type getReturnType() {
        return method.getGenericReturnType();
    }
}
