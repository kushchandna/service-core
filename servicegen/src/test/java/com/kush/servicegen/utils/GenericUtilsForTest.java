package com.kush.servicegen.utils;

import static com.kush.utils.commons.GenericsUtils.getGenericReturnTypeName;
import static com.kush.utils.commons.GenericsUtils.getGenericTypeName;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class GenericUtilsForTest {

    public static void assertGenericParameterType(Parameter parameter, Type outerType, Type innerType) {
        String actualTypeName = getGenericTypeName(parameter);
        String expectedTypeName = getGenericTypeName(outerType, innerType);
        assertThat(actualTypeName, is(equalTo(expectedTypeName)));
    }

    public static void assertGenericMethodReturnType(Method method, Type outerType, Type innerType, Type innermostType) {
        String actualReturnTypeName = getGenericReturnTypeName(method);
        String expectedReturnTypeName = getGenericTypeName(outerType, innerType, innermostType);
        assertThat(actualReturnTypeName, is(equalTo(expectedReturnTypeName)));
    }

    public static void assertGenericMethodReturnType(Method method, Type outerType, Type innerType) {
        String actualReturnTypeName = getGenericReturnTypeName(method);
        String expectedReturnTypeName = getGenericTypeName(outerType, innerType);
        assertThat(actualReturnTypeName, is(equalTo(expectedReturnTypeName)));
    }

    public static void assertMethodReturnType(Method method, Type expectedType) {
        assertThat(method.getReturnType(), is(equalTo(expectedType)));
    }
}
