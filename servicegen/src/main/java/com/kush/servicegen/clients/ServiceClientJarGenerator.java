package com.kush.servicegen.clients;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.google.common.io.Files;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.CodeGenerator;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceReader;
import com.kush.servicegen.javapoet.JavapoetBasedServiceClientCodeGenerator;
import com.kush.utils.commons.JarUtils;

public class ServiceClientJarGenerator {

    private final File targetDirectory;
    private final String targetJarName;
    private final ServiceReader serviceReader;

    public ServiceClientJarGenerator(File targetDirectory, String targetJarName) {
        this.targetDirectory = targetDirectory;
        this.targetJarName = targetJarName;
        serviceReader = new ServiceReader();
    }

    public JarFile generate(Collection<ServiceClientInfo> serviceClientInfos)
            throws ClassNotFoundException, CodeGenerationFailedException, IOException {
        File sourceDir = Files.createTempDir();
        File binDir = Files.createTempDir();
        for (ServiceClientInfo serviceClientInfo : serviceClientInfos) {
            String targetPackage = serviceClientInfo.getTargetPackage();
            Class<?> serviceClass = getServiceClass(serviceClientInfo);
            CodeGenerator codeGenerator = createCodeGenerator(serviceClass);
            JavaFileObject javaFileObject = codeGenerator.generate(targetPackage, sourceDir);
            compileGeneratedFile(javaFileObject, binDir.getAbsolutePath());
        }
        return generateJar(binDir);
    }

    private void compileGeneratedFile(JavaFileObject generatedFileObject, String targetDirectory) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> options = asList("-d", targetDirectory);
        CompilationTask task = compiler.getTask(null, null, null, options, null, asList(generatedFileObject));
        task.call();
    }

    private JarFile generateJar(File parentDir) throws FileNotFoundException, IOException {
        targetDirectory.mkdirs();
        File targetJarFile = new File(targetDirectory, targetJarName);
        OutputStream fos = new FileOutputStream(targetJarFile);
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream jarOs = new JarOutputStream(fos, manifest)) {
            File[] files = parentDir.listFiles();
            for (File file : files) {
                JarUtils.addToJar(file, jarOs);
            }
        }
        return new JarFile(targetJarFile);
    }

    private CodeGenerator createCodeGenerator(Class<?> serviceClass) {
        ServiceInfo serviceInfo = serviceReader.readService(serviceClass);
        return new JavapoetBasedServiceClientCodeGenerator(serviceInfo);
    }

    private Class<?> getServiceClass(ServiceClientInfo serviceClientInfo) throws ClassNotFoundException {
        String serviceClassName = serviceClientInfo.getServiceClassName();
        return Class.forName(serviceClassName);
    }
}
