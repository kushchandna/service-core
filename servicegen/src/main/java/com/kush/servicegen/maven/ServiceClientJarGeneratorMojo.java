package com.kush.servicegen.maven;

import static java.util.Arrays.asList;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceReader;
import com.kush.servicegen.javapoet.JavapoetBasedServiceClientCodeGenerator;

@Mojo(name = "service-client-generator")
public class ServiceClientJarGeneratorMojo extends AbstractMojo {

    private boolean cleanupSourceFiles;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String serviceClassName = null;
        try {
            generateServiceClient(serviceClassName, null, null);
        } catch (ClassNotFoundException | CodeGenerationFailedException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private void generateServiceClient(String serviceClassName, String targetPackage, File targetDirectory)
            throws ClassNotFoundException, CodeGenerationFailedException {
        Class<?> serviceClass = Class.forName(serviceClassName);
        ServiceReader serviceReader = new ServiceReader();
        ServiceInfo serviceInfo = serviceReader.readService(serviceClass);
        CodeGenerator codeGenerator = new JavapoetBasedServiceClientCodeGenerator(serviceInfo);
        JavaFileObject generatedJavaFileObject = codeGenerator.generate(targetPackage, targetDirectory);
    }

    private static void compileGeneratedFile(JavaFileObject generatedFileObject, String targetDirectory) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = asList("-d", targetDirectory);
        CompilationTask task = compiler.getTask(null, null, null, options, null, asList(generatedFileObject));
        task.call();
    }

    private static void loadCompiledClass(File generatedFile, String targetClientPackage, String generatedServiceClientName)
            throws Exception {
        URL generatedFileUrl = generatedFile.toURI().toURL();
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { generatedFileUrl })) {
            urlClassLoader.loadClass(targetClientPackage + "." + generatedServiceClientName);
        }
    }
}
