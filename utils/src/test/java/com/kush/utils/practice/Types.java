package com.kush.utils.practice;

import static java.util.Arrays.stream;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

public class Types {

    @Test
    public void parameterisedType() throws Exception {
        Method method = stream(Types.class.getDeclaredMethods())
            .filter(m -> m.getName().equals("test"))
            .findFirst()
            .get();
        Parameter parameter = method.getParameters()[0];
        ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
        System.out.println(parameterizedType);
        System.out.println("Raw Type: " + parameterizedType.getRawType());
        System.out.println("Owner Type: " + parameterizedType.getOwnerType());
        System.out.println("Type Name: " + parameterizedType.getTypeName());
        System.out.println("Actual Type Arguments: " + Arrays.toString(parameterizedType.getActualTypeArguments()));
        System.out.println("Class: " + parameterizedType.getClass());
        System.out.println("Type: " + parameter.getType());
    }

    static void test(Map<String, Integer> map) {
    }
}
