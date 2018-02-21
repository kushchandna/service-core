package com.kush.servicegen;

import java.io.File;

import com.kush.lib.service.server.BaseService;
import com.kush.servicegen.javapoet.JavapoetBasedServiceClientCodeGenerator;

public class ServiceClientCodeGeneratorFactory {

    private final ServiceReader serviceReader;

    public ServiceClientCodeGeneratorFactory() {
        serviceReader = new ServiceReader();
    }

    public CodeGenerator getServiceClientGenerator(Class<? extends BaseService> serviceClass, String targetPackage,
            File targetDirectory) {
        ServiceInfo serviceInfo = serviceReader.readService(serviceClass);
        return new JavapoetBasedServiceClientCodeGenerator(serviceInfo);
    }
}
