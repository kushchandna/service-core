package com.kush.servicegen.javapoet;

import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import com.google.common.primitives.Primitives;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.MethodInfo;
import com.kush.servicegen.ParameterInfo;
import com.kush.servicegen.ServiceInfo;
import com.kush.utils.async.Response;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

public class JavapoetBasedServiceClientCodeGenerator implements CodeGenerator {

    private final ServiceInfo serviceInfo;

    public JavapoetBasedServiceClientCodeGenerator(ServiceInfo serviceInfo) {
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
            .superclass(ServiceClient.class)
            .build();
    }

    private List<MethodSpec> createMethodSpecs(ServiceInfo serviceInfo) throws CodeGenerationFailedException {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (MethodInfo serviceMethod : serviceInfo.getServiceMethods()) {
            MethodSpec methodSpec = createServiceClientMethodSpec(serviceMethod);
            methodSpecs.add(methodSpec);
        }
        return methodSpecs;
    }

    private MethodSpec createServiceClientMethodSpec(MethodInfo serviceMethod) {
        String methodName = serviceMethod.getName();
        Type returnType = wrapType(serviceMethod.getReturnType());
        List<ParameterSpec> parameterSpecs = createParameterSpecs(serviceMethod);
        ParameterizedTypeName responseReturnTypeName = ParameterizedTypeName.get(Response.class, returnType);
        return MethodSpec.methodBuilder(methodName)
            .returns(responseReturnTypeName)
            .addModifiers(PUBLIC)
            .addParameters(parameterSpecs)
            .addStatement(createInvokeStatement(parameterSpecs), methodName, returnType.getTypeName())
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

    private String createInvokeStatement(List<ParameterSpec> paramSpecs) {
        StringBuilder builder = new StringBuilder("return invoke(");
        builder.append("$S, $L.class");
        for (ParameterSpec paramSpec : paramSpecs) {
            builder.append(',').append(" ").append(paramSpec.name);
        }
        builder.append(")");
        return builder.toString();
    }

    private String getServiceApiName(ServiceInfo serviceInfo) {
        return serviceInfo.getName() + "Client";
    }

    private Type wrapType(Type returnType) {
        if (returnType instanceof Class<?>) {
            returnType = Primitives.wrap((Class<?>) returnType);
        }
        return returnType;
    }
}
