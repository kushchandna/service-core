package com.kush.servicegen;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.kush.lib.service.server.authentication.AuthenticationRequired;

public class ServiceMethodInfo {

    private final Method method;

    public ServiceMethodInfo(Method serviceMethod) {
        method = serviceMethod;
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

    public boolean isAuthenticationRequired() {
        return method.getAnnotation(AuthenticationRequired.class) != null;
    }

    public Type getReturnType() {
        return method.getGenericReturnType();
    }
}
