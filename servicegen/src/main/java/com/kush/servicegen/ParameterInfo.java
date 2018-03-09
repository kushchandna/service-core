package com.kush.servicegen;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

import com.kush.lib.service.server.annotations.Exportable;

public class ParameterInfo {

    private final Parameter parameter;

    public ParameterInfo(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter.getName();
    }

    public Type getParameterizedType() {
        return parameter.getParameterizedType();
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public boolean isExportable() {
        return getType().isAnnotationPresent(Exportable.class);
    }
}
