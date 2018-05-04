package com.kush.servicegen.clients;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.kush.lib.service.server.annotations.Exportable;
import com.kush.lib.service.server.authentication.LoginService;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.ServiceInfo;
import com.kush.servicegen.ServiceReader;
import com.kush.servicegen.javapoet.JavapoetBasedServiceClientCodeGenerator;
import com.kush.utils.commons.JarUtils;
import com.kush.utils.commons.ReferedClassesFinder;

public class ServiceClientJarGenerator {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ServiceClientJarGenerator.class);

    private final File targetDirectory;
    private final String targetJarName;
    private final ServiceReader serviceReader;
    private final ReferedClassesFinder referedClassesFinder;

    public ServiceClientJarGenerator(File targetDirectory, String targetJarName) {
        this.targetDirectory = targetDirectory;
        this.targetJarName = targetJarName;
        serviceReader = new ServiceReader();
        referedClassesFinder = new ReferedClassesFinder(klass -> klass.isAnnotationPresent(Exportable.class));
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
            compileGeneratedFile(javaFileObject, binDir.getAbsolutePath());
        }
        LOGGER.info("Exporting requires exportable classes %s", classesToExport);
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
