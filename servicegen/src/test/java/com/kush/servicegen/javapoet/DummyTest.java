package com.kush.servicegen.javapoet;

import java.lang.reflect.Type;

import org.junit.Test;

import com.kush.lib.service.client.api.Response;
import com.squareup.javapoet.ParameterizedTypeName;

public class DummyTest {

    @Test
    public void testName() throws Exception {
        Type returnType = Void.class;
        ParameterizedTypeName responseReturnTypeName = ParameterizedTypeName.get(Response.class, returnType);
        System.out.println(responseReturnTypeName);
    }
}
