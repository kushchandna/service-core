package com.kush.servicegen.javapoet;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import com.kush.lib.service.remoting.api.ServiceApi;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.MethodInfo;
import com.kush.servicegen.ParameterInfo;
import com.kush.servicegen.ServiceInfo;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

public class JavapoetBasedServiceApiCodeGenerator implements CodeGenerator {

    private final ServiceInfo serviceInfo;

    public JavapoetBasedServiceApiCodeGenerator(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    @Override
    public JavaFileObject generate(String targetPackage, File targetDirectory) throws CodeGenerationFailedException {
        JavaFile javaFile = createJavaFile(targetPackage, serviceInfo);
        try {
            javaFile.writeTo(targetDirectory);
            return javaFile.toJavaFileObject();
        } catch (IOException e) {
            throw new CodeGenerationFailedException(e.getMessage(), e);
        }
    }

    private JavaFile createJavaFile(String targetPackage, ServiceInfo serviceInfo) {
        TypeSpec typeSpec = createTypeSpec(serviceInfo);
        return JavaFile.builder(targetPackage, typeSpec)
            .skipJavaLangImports(true)
            .build();
    }

    private TypeSpec createTypeSpec(ServiceInfo serviceInfo) {
        List<MethodSpec> methodSpecs = createMethodSpecs(serviceInfo);
        return TypeSpec.interfaceBuilder(getServiceApiName(serviceInfo))
            .addModifiers(PUBLIC)
            .addMethods(methodSpecs)
            .addSuperinterface(ServiceApi.class)
            .build();
    }

    private List<MethodSpec> createMethodSpecs(ServiceInfo serviceInfo) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (MethodInfo serviceMethod : serviceInfo.getServiceMethods()) {
            List<ParameterSpec> parameterSpecs = createParameterSpecs(serviceMethod);
            MethodSpec methodSpec = MethodSpec.methodBuilder(serviceMethod.getName())
                .addModifiers(PUBLIC, ABSTRACT)
                .returns(serviceMethod.getReturnType())
                .addParameters(parameterSpecs)
                .build();
            methodSpecs.add(methodSpec);
        }
        return methodSpecs;
    }

    private List<ParameterSpec> createParameterSpecs(MethodInfo serviceMethod) {
        List<ParameterInfo> parameterInfos = serviceMethod.getParameters();
        List<ParameterSpec> parameterSpecs = new ArrayList<>();
        for (ParameterInfo parameterInfo : parameterInfos) {
            ParameterSpec paramSpec = ParameterSpec.builder(parameterInfo.getType(), parameterInfo.getName())
                .build();
            parameterSpecs.add(paramSpec);
        }
        return parameterSpecs;
    }

    private String getServiceApiName(ServiceInfo serviceInfo) {
        return serviceInfo.getName() + "Api";
    }
}
