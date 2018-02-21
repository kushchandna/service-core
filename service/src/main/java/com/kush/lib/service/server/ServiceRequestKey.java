package com.kush.lib.service.server;

public final class ServiceRequestKey {

    private final String serviceName;
    private final String methodName;

    public ServiceRequestKey(String serviceName, String methodName) {
        this.serviceName = serviceName;
        this.methodName = methodName;
    }

    String getServiceName() {
        return serviceName;
    }

    String getMethodName() {
        return methodName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (methodName == null ? 0 : methodName.hashCode());
        result = prime * result + (serviceName == null ? 0 : serviceName.hashCode());
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
        ServiceRequestKey other = (ServiceRequestKey) obj;
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
