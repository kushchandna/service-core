package com.kush.servicegen.utils;

import static com.kush.utils.commons.TypeUtils.getGenericTypeName;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class TypeUtilsForTest {

    public static void assertGenericParameterType(Parameter parameter, Type outerType, Type innerType) {
        String actualTypeName = getGenericParameterType(parameter);
        String expectedTypeName = getGenericTypeName(outerType, innerType);
        assertThat(actualTypeName, is(equalTo(expectedTypeName)));
    }

    public static void assertGenericMethodReturnType(Method method, Type... typesInOrder) {
        String actualReturnTypeName = getGenericReturnTypeName(method);
        String expectedReturnTypeName = getGenericTypeName(typesInOrder);
        assertThat(actualReturnTypeName, is(equalTo(expectedReturnTypeName)));
    }

    public static void assertMethodReturnType(Method method, Type expectedType) {
        assertThat(method.getReturnType(), is(equalTo(expectedType)));
    }

    private static String getGenericReturnTypeName(Method method) {
        return method.getGenericReturnType().getTypeName();
    }

    private static String getGenericParameterType(Parameter parameter) {
        return parameter.getParameterizedType().getTypeName();
    }
}
