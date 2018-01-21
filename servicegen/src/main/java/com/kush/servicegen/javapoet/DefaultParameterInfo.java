package com.kush.servicegen.javapoet;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import com.kush.servicegen.api.ParameterInfo;

public class DefaultParameterInfo implements ParameterInfo {

    private final Parameter parameter;

    public DefaultParameterInfo(Parameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getName() {
        return parameter.getName();
    }

    @Override
    public Type getType() {
        return parameter.getParameterizedType();
    }
}
