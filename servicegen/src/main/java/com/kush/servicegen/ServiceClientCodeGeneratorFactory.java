package com.kush.servicegen;

import java.io.File;

import com.kush.lib.service.server.api.BaseService;
import com.kush.servicegen.javapoet.JavapoetBasedServiceApiCodeGenerator;

public class ServiceClientCodeGeneratorFactory {

    private final ServiceReader serviceReader;

    public ServiceClientCodeGeneratorFactory() {
        serviceReader = new ServiceReader();
    }

    public CodeGenerator getServiceApiGenerator(Class<? extends BaseService> serviceClass, String targetPackage,
            File targetDirectory) {
        ServiceInfo serviceInfo = serviceReader.readService(serviceClass);
        return new JavapoetBasedServiceApiCodeGenerator(serviceInfo);
    }

    public CodeGenerator getServiceClientGenerator(Class<? extends BaseService> serviceClass, String targetPackage,
            File targetDirectory) {
        ServiceInfo serviceInfo = serviceReader.readService(serviceClass);
        return new JavapoetBasedServiceApiCodeGenerator(serviceInfo);
    }
}
