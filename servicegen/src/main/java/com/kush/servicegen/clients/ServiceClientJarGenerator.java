package com.kush.servicegen.clients;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.google.common.io.Files;
import com.kush.lib.service.server.annotations.Exportable;
import com.kush.servicegen.CodeGenerationFailedException;
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

    public File generate(List<String> services) throws CodeGenerationFailedException {
        try {
            return generateJarAndReturnFile(services);
        } catch (ClassNotFoundException | IOException e) {
            throw new CodeGenerationFailedException(e.getMessage(), e);
        }
    }

    private File generateJarAndReturnFile(List<String> services)
            throws ClassNotFoundException, CodeGenerationFailedException, IOException, FileNotFoundException {
        File generatedFilesDir = new File(targetDirectory, "generated");
        generatedFilesDir.mkdirs();
        File sourceDir = new File(generatedFilesDir, "sources");
        sourceDir.mkdirs();
        File binDir = new File(generatedFilesDir, "bin");
        binDir.mkdirs();
        Set<Class<?>> classesToExport = new HashSet<>();
        for (String serviceClassName : services) {
            Class<?> serviceClass = loadClass(serviceClassName);
            addClassesToExport(serviceClass, classesToExport);
            JavapoetBasedServiceClientCodeGenerator codeGenerator = createCodeGenerator(serviceClass);
            String targetPackage = getTargetPackage(serviceClass);
            JavaFileObject javaFileObject = codeGenerator.generate(targetPackage, sourceDir);
            compileGeneratedFile(javaFileObject, binDir.getAbsolutePath());
        }
        for (Class<?> classToExport : classesToExport) {
            File classFile = JarUtils.getClassFile(classToExport);
            String targetClassName = classToExport.getName();
            String targetClassRelativePath = targetClassName.replace('.', '/') + ".class";
            File targetClassFile = new File(binDir, targetClassRelativePath);
            targetClassFile.getParentFile().mkdirs();
            targetClassFile.createNewFile();
            Files.copy(classFile, targetClassFile);
        }
        return generateJar(binDir);
    }

    private void addClassesToExport(Class<?> serviceClass, Set<Class<?>> classesToExport) {
        Field[] fields = serviceClass.getFields();
        for (Field field : fields) {
            addIfRequired(classesToExport, field.getType());
        }
        Method[] methods = serviceClass.getMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                addIfRequired(classesToExport, parameter.getType());
            }
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            for (Class<?> exceptionType : exceptionTypes) {
                addIfRequired(classesToExport, exceptionType);
            }
        }
    }

    private void addIfRequired(Set<Class<?>> classesToExport, Class<?> type) {
        if (type.isAnnotationPresent(Exportable.class)) {
            if (!classesToExport.contains(type)) {
                classesToExport.add(type);
                addClassesToExport(type, classesToExport);
            }
        }
    }

    private Class<?> loadClass(String className) throws ClassNotFoundException {
        return Class.forName(className, true, getClass().getClassLoader());
    }

    private String getTargetPackage(Class<?> serviceClass) {
        return serviceClass.getPackage().getName() + ".servicegen.generated.clients";
    }

    private void compileGeneratedFile(JavaFileObject generatedFileObject, String targetDirectory) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String classPath = Arrays.toString(getClassPath()).replace(", ", ";").replace('\\', '/');
        String classPathText = "\"" + classPath.substring(1, classPath.length() - 1) + "\"";
        Iterable<String> options = Arrays.asList(
                "-d", targetDirectory,
                "-cp", classPathText);
        CompilationTask task = compiler.getTask(null, null, null, options, null, asList(generatedFileObject));
        task.call();
    }

    private File[] getClassPath() {
        URLClassLoader classLoader = (URLClassLoader) getClass().getClassLoader();
        URL[] urls = classLoader.getURLs();
        return Arrays.stream(urls).map(url -> new File(url.getFile())).toArray(File[]::new);
    }

    private File generateJar(File parentDir) throws FileNotFoundException, IOException {
        targetDirectory.mkdirs();
        File targetJarFile = new File(targetDirectory, targetJarName);
        OutputStream fos = new FileOutputStream(targetJarFile);
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream jarOS = new JarOutputStream(fos, manifest)) {
            File[] files = parentDir.listFiles();
            for (File file : files) {
                JarUtils.addToJar(parentDir, file, jarOS);
            }
        }
        return targetJarFile;
    }

    private JavapoetBasedServiceClientCodeGenerator createCodeGenerator(Class<?> serviceClass) {
        ServiceInfo serviceInfo = serviceReader.readService(serviceClass);
        return new JavapoetBasedServiceClientCodeGenerator(serviceInfo);
    }
}
