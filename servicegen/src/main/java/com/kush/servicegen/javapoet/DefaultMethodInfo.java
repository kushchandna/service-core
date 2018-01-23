package com.kush.servicegen.javapoet;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.kush.servicegen.api.MethodInfo;
import com.kush.servicegen.api.ParameterInfo;

class DefaultMethodInfo implements MethodInfo {

    private final Method method;

    public DefaultMethodInfo(Method method) {
        this.method = method;
    }

    @Override
    public String getName() {
        return method.getName();
    }

    @Override
    public List<ParameterInfo> getParameters() {
        List<ParameterInfo> paramInfos = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            ParameterInfo paramInfo = new DefaultParameterInfo(parameter);
            paramInfos.add(paramInfo);
        }
        return paramInfos;
    }

    @Override
    public Type getReturnType() {
        return method.getGenericReturnType();
    }
}
