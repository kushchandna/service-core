package com.kush.servicegen.api;

import java.lang.reflect.Type;
import java.util.List;

public interface MethodInfo {

    String getName();

    List<ParameterInfo> getParameters();

    Type getReturnType();
}