package com.kush.lib.service.remoting.api;

import java.io.Serializable;

public class ServiceInvocationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String serviceName;
    private final String methodName;
    private final Object[] args;

    public ServiceInvocationRequest(String serviceName, String methodName, Object... args) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.args = args;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }
}
