package com.kush.servicegen;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class ParameterInfo {

    private final Parameter parameter;

    public ParameterInfo(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter.getName();
    }

    public Type getType() {
        return parameter.getParameterizedType();
    }
}
