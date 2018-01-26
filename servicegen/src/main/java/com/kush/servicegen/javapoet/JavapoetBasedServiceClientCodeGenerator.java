package com.kush.servicegen.javapoet;

import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import com.google.common.base.Defaults;
import com.google.common.primitives.Primitives;
import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.MethodInfo;
import com.kush.servicegen.ParameterInfo;
import com.kush.servicegen.ServiceInfo;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

public class JavapoetBasedServiceClientCodeGenerator implements CodeGenerator {

    private final ServiceInfo serviceInfo;
    private final Type serviceApiType;

    public JavapoetBasedServiceClientCodeGenerator(ServiceInfo serviceInfo, Type serviceApiType) {
        this.serviceInfo = serviceInfo;
        this.serviceApiType = serviceApiType;
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

    private JavaFile createJavaFile(String targetPackage, ServiceInfo serviceInfo) throws CodeGenerationFailedException {
        TypeSpec typeSpec = createTypeSpec(serviceInfo);
        return JavaFile.builder(targetPackage, typeSpec)
            .skipJavaLangImports(true)
            .build();
    }

    private TypeSpec createTypeSpec(ServiceInfo serviceInfo) throws CodeGenerationFailedException {
        List<MethodSpec> methodSpecs = createMethodSpecs(serviceInfo);
        return TypeSpec.classBuilder(getServiceApiName(serviceInfo))
            .addModifiers(PUBLIC)
            .addMethods(methodSpecs)
            .superclass(ParameterizedTypeName.get(ServiceClient.class, serviceApiType))
            .build();
    }

    private List<MethodSpec> createMethodSpecs(ServiceInfo serviceInfo) throws CodeGenerationFailedException {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (MethodInfo serviceMethod : serviceInfo.getServiceMethods()) {
            List<ParameterSpec> parameterSpecs = createParameterSpecs(serviceMethod);
            MethodSpec methodSpec = createMethodBuilder(serviceMethod)
                .addModifiers(PUBLIC)
                .addParameters(parameterSpecs)
                .build();
            methodSpecs.add(methodSpec);
        }
        MethodSpec getServiceApiClassMethod = createGetServiceApiClassMethod();
        methodSpecs.add(getServiceApiClassMethod);
        return methodSpecs;
    }

    private Builder createMethodBuilder(MethodInfo serviceMethod) throws CodeGenerationFailedException {
        Type returnType = serviceMethod.getReturnType();
        if (returnType instanceof Class<?>) {
            returnType = Primitives.wrap((Class<?>) returnType);
        }
        ParameterizedTypeName responseReturnTypeName = ParameterizedTypeName.get(Response.class, returnType);
        Builder methodBuilder = MethodSpec.methodBuilder(serviceMethod.getName())
            .returns(responseReturnTypeName);
        enrichReturnStatement(returnType, methodBuilder);
        return methodBuilder;
    }

    private void enrichReturnStatement(Type returnType, Builder methodBuilder) throws CodeGenerationFailedException {
        if (returnType == void.class) {
            return;
        }
        Object defaultValue = null;
        if (returnType instanceof Class<?>) {
            defaultValue = Defaults.defaultValue((Class<?>) returnType);
        }
        methodBuilder.addStatement("return $L", defaultValue);
    }

    private MethodSpec createGetServiceApiClassMethod() {
        return MethodSpec.methodBuilder("getServiceApiClass")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Override.class)
            .addStatement("return $L.class", serviceApiType.getTypeName())
            .returns(ParameterizedTypeName.get(Class.class, serviceApiType))
            .build();
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
        return serviceInfo.getName() + "Client";
    }
}
