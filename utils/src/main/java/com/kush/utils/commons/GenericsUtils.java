package com.kush.utils.commons;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class GenericsUtils {

    private static final String GENERIC_NAME_TEMPLATE = "%s<%s>";

    public static String getGenericTypeName(String outerTypeName, String innerTypeName) {
        return String.format(GENERIC_NAME_TEMPLATE, outerTypeName, innerTypeName);
    }

    public static String getGenericTypeName(Type outerType, Type innerType, Type innermostType) {
        String innerName = getGenericTypeName(innerType, innermostType);
        return getGenericTypeName(outerType, innerName);
    }

    public static String getGenericTypeName(Type outerType, Type innerType) {
        return getGenericTypeName(outerType.getTypeName(), innerType.getTypeName());
    }

    public static String getGenericTypeName(Type outerType, String innerTypeName) {
        return getGenericTypeName(outerType.getTypeName(), innerTypeName);
    }

    public static String getGenericTypeName(String outerTypeName, Type innerType) {
        return getGenericTypeName(outerTypeName, innerType.getTypeName());
    }

    public static String getGenericTypeName(Parameter parameter) {
        return parameter.getParameterizedType().getTypeName();
    }

    public static String getGenericReturnTypeName(Method method) {
        return method.getGenericReturnType().getTypeName();
    }
}
