package com.kush.servicegen;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MethodInfo {

    private final Method method;

    public MethodInfo(Method method) {
        this.method = method;
    }

    public String getName() {
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
