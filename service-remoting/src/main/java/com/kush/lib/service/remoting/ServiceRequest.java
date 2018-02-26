package com.kush.lib.service.remoting;

import java.io.Serializable;

import com.kush.lib.service.remoting.auth.AuthToken;

public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final AuthToken authToken;
    private final String serviceName;
    private final String methodName;
    private final Object[] args;

    public ServiceRequest(AuthToken authToken, String serviceName, String methodName, Object... args) {
        this.authToken = authToken;
        this.serviceName = serviceName;
        this.methodName = methodName;
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
}
