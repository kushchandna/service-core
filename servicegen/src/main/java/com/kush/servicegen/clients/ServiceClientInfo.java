package com.kush.servicegen.clients;

public class ServiceClientInfo {

    private String serviceClassName;
    private String targetPackage;

    public String getServiceClassName() {
        return serviceClassName;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }
}
