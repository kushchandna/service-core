package com.kush.lib.service.remoting;

import java.io.Serializable;

import com.kush.lib.service.remoting.ServiceRequestResolver.ReturnType;
import com.kush.lib.service.remoting.auth.AuthToken;

public class ServiceRequest<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AuthToken authToken;
    private final String serviceName;
    private final String methodName;
    private final ReturnType<T> returnType;
    private final Object[] args;

    public ServiceRequest(AuthToken authToken, String serviceName, String methodName, ReturnType<T> returnType, Object... args) {
        this.authToken = authToken;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.args = args;
    }

    public AuthToken getAuthToken() {
        return authToken;
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

    public ReturnType<T> getReturnType() {
        return returnType;
    }
}
