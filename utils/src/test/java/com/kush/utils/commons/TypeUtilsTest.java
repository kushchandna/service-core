package com.kush.utils.commons;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.Test;

public class TypeUtilsTest {

    @Test
    public void getGenericType_WithOneArgument() throws Exception {
        String genericTypeName = TypeUtils.getGenericTypeName(String.class);
        assertThat(genericTypeName, is(equalTo("java.lang.String")));
    }

    @Test
    public void getGenericType_WithTwoArguments() throws Exception {
        String genericTypeName = TypeUtils.getGenericTypeName(Set.class, String.class);
        assertThat(genericTypeName, is(equalTo("java.util.Set<java.lang.String>")));
    }

    @Test
    public void getGenericType_WithThreeArguments() throws Exception {
        String genericTypeName = TypeUtils.getGenericTypeName(List.class, Set.class, String.class);
        assertThat(genericTypeName, is(equalTo("java.util.List<java.util.Set<java.lang.String>>")));
    }

    @Test
    public void getGenericType_WithFourArguments() throws Exception {
        String genericTypeName = TypeUtils.getGenericTypeName(List.class, Set.class, Class.class, String.class);
        assertThat(genericTypeName, is(equalTo("java.util.List<java.util.Set<java.lang.Class<java.lang.String>>>")));
    }
}
