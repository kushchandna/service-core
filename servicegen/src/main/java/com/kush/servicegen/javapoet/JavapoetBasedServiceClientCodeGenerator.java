package com.kush.servicegen.javapoet;

import static javax.lang.model.element.Modifier.PUBLIC;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.tools.JavaFileObject;

import com.google.common.primitives.Primitives;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.server.annotations.Exportable;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.ParameterInfo;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceMethodInfo;
import com.kush.utils.async.Response;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

public class JavapoetBasedServiceClientCodeGenerator implements CodeGenerator {

    private final ServiceInfo serviceInfo;
    private final Set<Class<?>> classesToExport = new HashSet<>();

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

    // TODO improve this method (it should not be here)
    public Set<Class<?>> getClassesToExport() {
        return Collections.unmodifiableSet(classesToExport);
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
            .addJavadoc("@generated\n")
            .build();
    }

    private List<MethodSpec> createMethodSpecs(ServiceInfo serviceInfo) throws CodeGenerationFailedException {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        MethodSpec constructor = createServiceClientConstructor(serviceInfo);
        methodSpecs.add(constructor);
        for (ServiceMethodInfo serviceMethod : serviceInfo.getServiceMethods()) {
            MethodSpec methodSpec = createServiceClientMethodSpec(serviceMethod);
            methodSpecs.add(methodSpec);
        }
        return methodSpecs;
    }

    private MethodSpec createServiceClientConstructor(ServiceInfo serviceInfo) {
        return MethodSpec.constructorBuilder()
            .addStatement("super($S)", serviceInfo.getServiceId())
            .addModifiers(PUBLIC)
            .build();
    }

    private MethodSpec createServiceClientMethodSpec(ServiceMethodInfo serviceMethod) {
        String methodName = serviceMethod.getMethodName();
        Type actualReturnType = serviceMethod.getReturnType();
        addToClassesToExportIfRequired(actualReturnType);
        Type adaptedReturnType = wrapType(actualReturnType);
        List<ParameterSpec> parameterSpecs = createParameterSpecs(serviceMethod);
        ParameterizedTypeName responseReturnTypeName = ParameterizedTypeName.get(Response.class, adaptedReturnType);
        return MethodSpec.methodBuilder(methodName)
            .returns(responseReturnTypeName)
            .addModifiers(PUBLIC)
            .addParameters(parameterSpecs)
            .addStatement(createInvokeStatement(parameterSpecs, serviceMethod.isAuthenticationRequired()),
                    serviceMethod.getServiceMethodId())
            .build();
    }

    private void addToClassesToExportIfRequired(Type actualReturnType) {
        if (actualReturnType instanceof Class<?>) {
            Class<?> returnTypeClass = (Class<?>) actualReturnType;
            if (returnTypeClass.isAnnotationPresent(Exportable.class)) {
                classesToExport.add(returnTypeClass);
            }
        }
    }

    private List<ParameterSpec> createParameterSpecs(ServiceMethodInfo serviceMethod) {
        List<ParameterInfo> parameterInfos = serviceMethod.getParameters();
        List<ParameterSpec> parameterSpecs = new ArrayList<>();
        for (ParameterInfo parameterInfo : parameterInfos) {
            if (parameterInfo.isExportable()) {
                classesToExport.add(parameterInfo.getType());
            }
            ParameterSpec paramSpec = ParameterSpec.builder(parameterInfo.getParameterizedType(), parameterInfo.getName())
                .build();
            parameterSpecs.add(paramSpec);
        }
        return parameterSpecs;
    }

    private String createInvokeStatement(List<ParameterSpec> paramSpecs, boolean authenticationRequired) {
        String invokeText = authenticationRequired ? "authInvoke" : "invoke";
        StringBuilder builder = new StringBuilder("return");
        builder.append(" ").append(invokeText).append("(").append("$S");
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
