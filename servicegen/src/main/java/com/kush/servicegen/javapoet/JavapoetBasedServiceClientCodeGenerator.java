package com.kush.servicegen.javapoet;

import static java.lang.String.format;
import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import com.google.common.primitives.Primitives;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.MethodInfo;
import com.kush.servicegen.ParameterInfo;
import com.kush.servicegen.ServiceInfo;
import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Response;
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
            MethodSpec methodSpec = createServiceClientMethodSpec(serviceMethod);
            methodSpecs.add(methodSpec);
        }
        MethodSpec getServiceApiClassMethod = createGetServiceApiClassMethod();
        methodSpecs.add(getServiceApiClassMethod);
        return methodSpecs;
    }

    private MethodSpec createServiceClientMethodSpec(MethodInfo serviceMethod) {
        String methodName = serviceMethod.getName();
        Type returnType = wrapType(serviceMethod.getReturnType());
        List<ParameterSpec> parameterSpecs = createParameterSpecs(serviceMethod);
        TypeSpec serviceTaskTypeSpec = createServiceTaskAnonymousClass(methodName, returnType, parameterSpecs);
        ParameterizedTypeName responseReturnTypeName = ParameterizedTypeName.get(Response.class, returnType);
        return MethodSpec.methodBuilder(methodName)
            .returns(responseReturnTypeName)
            .addModifiers(PUBLIC)
            .addParameters(parameterSpecs)
            .addStatement("return invoke($L)", serviceTaskTypeSpec)
            .build();
    }

    private TypeSpec createServiceTaskAnonymousClass(String methodName, Type returnType, List<ParameterSpec> parameterSpecs) {
        MethodSpec executeMethodSpec = createServiceTaskExecuteMethod(methodName, returnType, parameterSpecs);
        return TypeSpec.anonymousClassBuilder("")
            .addSuperinterface(ParameterizedTypeName.get(Request.class, returnType))
            .addMethod(executeMethodSpec)
            .build();
    }

    private MethodSpec createServiceTaskExecuteMethod(String methodName, Type returnType, List<ParameterSpec> parameterSpecs) {
        String serviceTaskMethodCallText = getServiceTaskMethodCallText(methodName, parameterSpecs);
        return createServiceTaskExecuteMethodSpecBuilder(returnType, serviceTaskMethodCallText)
            .addAnnotation(Override.class)
            .addModifiers(PUBLIC)
            .addException(RequestFailedException.class)
            .build();
    }

    private Builder createServiceTaskExecuteMethodSpecBuilder(Type returnType, String serviceTaskMethodCallText) {
        Builder methodBuilder = MethodSpec.methodBuilder("respond")
            .returns(returnType);
        if (Void.class.equals(returnType)) {
            methodBuilder.addStatement("getService().$L", serviceTaskMethodCallText)
                .addStatement("return null");
        } else {
            methodBuilder.addStatement("return getService().$L", serviceTaskMethodCallText);
        }
        return methodBuilder;
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

    private Type wrapType(Type returnType) {
        if (returnType instanceof Class<?>) {
            returnType = Primitives.wrap((Class<?>) returnType);
        }
        return returnType;
    }

    private String getServiceTaskMethodCallText(String methodName, List<ParameterSpec> parameterSpecs) {
        StringBuilder builder = new StringBuilder();
        if (!parameterSpecs.isEmpty()) {
            for (ParameterSpec parameterSpec : parameterSpecs) {
                builder.append(parameterSpec.name).append(",").append(" ");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }
        return format("%s(%s)", methodName, builder.toString());
    }
}
