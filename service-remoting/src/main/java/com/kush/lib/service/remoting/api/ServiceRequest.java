package com.kush.lib.service.remoting.api;

import java.io.Serializable;
import java.util.Arrays;

public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String serviceName;
    private final String methodName;
    private final Object[] args;

    public ServiceRequest(String serviceName, String methodName, Object... args) {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(args);
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((serviceName == null) ? 0 : serviceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ServiceRequest other = (ServiceRequest) obj;
        if (!Arrays.equals(args, other.args)) {
            return false;
        }
        if (methodName == null) {
            if (other.methodName != null) {
                return false;
            }
        } else if (!methodName.equals(other.methodName)) {
            return false;
        }
        if (serviceName == null) {
            if (other.serviceName != null) {
                return false;
            }
        } else if (!serviceName.equals(other.serviceName)) {
            return false;
        }
        return true;
    }
}
