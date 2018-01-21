package com.kush.servicegen.api;

import java.util.List;

public interface ServiceInfo {

    String getName();

    List<MethodInfo> getServiceMethods();
}