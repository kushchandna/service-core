package com.kush.servicegen.clients;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaFileObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kush.commons.utils.JavaUtils;
import com.kush.service.annotations.Exportable;
import com.kush.service.auth.LoginService;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceReader;
import com.kush.servicegen.javapoet.JavapoetBasedServiceClientCodeGenerator;
import com.kush.utils.commons.AssociatedClassesFinder;
import com.kush.utils.commons.JarUtils;

public class ServiceClientJarGenerator {

    private static final Logger LOGGER = LogManager.getFormatterLogger(ServiceClientJarGenerator.class);

    private final File targetDirectory;
    private final String targetJarName;
    private final ServiceReader serviceReader;
    private final AssociatedClassesFinder referedClassesFinder;

    public ServiceClientJarGenerator(File targetDirectory, String targetJarName) {
        this.targetDirectory = targetDirectory;
        this.targetJarName = targetJarName;
        serviceReader = new ServiceReader();
        referedClassesFinder = new AssociatedClassesFinder(klass -> klass.isAnnotationPresent(Exportable.class));
    }

    public File generate(Collection<Class<?>> services) throws CodeGenerationFailedException {
        LOGGER.info("Generating service clients for %s", services);
        try {
            return generateJarAndReturnFile(services);
        } catch (ClassNotFoundException | IOException e) {
            throw new CodeGenerationFailedException(e.getMessage(), e);
        }
    }

    private File generateJarAndReturnFile(Collection<Class<?>> services)
            throws ClassNotFoundException, CodeGenerationFailedException, IOException {
        File generatedFilesDir = new File(targetDirectory, "generated");
        generatedFilesDir.mkdirs();
        File sourceDir = new File(generatedFilesDir, "sources");
        sourceDir.mkdirs();
        File binDir = new File(generatedFilesDir, "bin");
        binDir.mkdirs();
        Set<Class<?>> classesToExport = new HashSet<>();
        for (Class<?> serviceClass : services) {
            if (serviceClass.equals(LoginService.class)) {
                continue;
            }
            classesToExport.addAll(referedClassesFinder.find(serviceClass));
            JavapoetBasedServiceClientCodeGenerator codeGenerator = createCodeGenerator(serviceClass);
            String targetPackage = getTargetPackage(serviceClass);
            JavaFileObject javaFileObject = codeGenerator.generate(targetPackage, sourceDir);
            compileGeneratedFile(binDir, javaFileObject);
        }
        LOGGER.info("Exporting requires exportable classes %s", classesToExport);
        List<Class<?>> nonSerializable =
                classesToExport.stream().filter(c -> !Serializable.class.isAssignableFrom(c)).collect(toList());
        if (!nonSerializable.isEmpty()) {
            throw new IllegalStateException("Can not export non-seriazable classes " + nonSerializable);
        }
        for (Class<?> classToExport : classesToExport) {
            LOGGER.info("Exporting %s", classToExport.getName());
            try {
                JarUtils.copyClassFile(classToExport, binDir);
                LOGGER.info("Exported %s", classToExport.getName());
            } catch (IOException e) {
                LOGGER.error(e);
                throw e;
            }
        }
        return generateJar(binDir);
    }

    private void compileGeneratedFile(File binDir, JavaFileObject javaFileObject) {
        JavaUtils.compile(binDir.getAbsolutePath(), javaFileObject);
    }

    private String getTargetPackage(Class<?> serviceClass) {
        return serviceClass.getPackage().getName() + ".servicegen.generated.clients";
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
