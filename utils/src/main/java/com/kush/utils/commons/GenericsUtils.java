package com.kush.utils.commons;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class GenericsUtils {

    private static final String GENERIC_NAME_TEMPLATE = "%s<%s>";

    public static String getGenericTypeName(String outerTypeName, String innerTypeName) {
        return String.format(GENERIC_NAME_TEMPLATE, outerTypeName, innerTypeName);
    }

    public static String getGenericTypeName(Class<?> outerType, Class<?> innerType) {
        return getGenericTypeName(outerType.getName(), innerType.getName());
    }

    public static String getGenericTypeName(Class<?> outerType, String innerTypeName) {
        return getGenericTypeName(outerType.getName(), innerTypeName);
    }

    public static String getGenericTypeName(String outerTypeName, Class<?> innerType) {
        return getGenericTypeName(outerTypeName, innerType.getName());
    }

    public static String getGenericTypeName(Parameter parameter) {
        return parameter.getParameterizedType().getTypeName();
    }

    public static String getGenericReturnTypeName(Method method) {
        return method.getGenericReturnType().getTypeName();
    }
}
