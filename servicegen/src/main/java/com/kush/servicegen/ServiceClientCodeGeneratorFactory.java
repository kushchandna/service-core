package com.kush.servicegen;

import java.io.File;

import com.kush.service.BaseService;
import com.kush.servicegen.clients.JavapoetBasedServiceClientCodeGenerator;

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
