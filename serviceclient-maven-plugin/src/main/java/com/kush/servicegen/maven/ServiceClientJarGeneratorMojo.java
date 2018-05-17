package com.kush.servicegen.maven;

import static com.kush.utils.commons.CollectionUtils.nonNull;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;

import com.kush.service.annotations.Service;
import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.clients.ServiceClientJarGenerator;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE)
public class ServiceClientJarGeneratorMojo extends AbstractMojo {

    private final Reflections reflections;

    @Component
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    private File targetDirectory;
    @Parameter(defaultValue = "generated.service.client.jar.path", readonly = true)
    private String generatedServiceClientJarPath;

    public ServiceClientJarGeneratorMojo() {
        reflections = new Reflections();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
        getLog().info("Found services " + services);
        String serviceClientJarName = prepareServiceClientJarName();
        ServiceClientJarGenerator jarGenerator = new ServiceClientJarGenerator(targetDirectory, serviceClientJarName);
        try {
            File jarFile = jarGenerator.generate(nonNull(services));
            Properties properties = mavenProject.getProperties();
            properties.setProperty(generatedServiceClientJarPath, jarFile.getAbsolutePath());
        } catch (CodeGenerationFailedException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private String prepareServiceClientJarName() {
        String projectName = mavenProject.getName();
        String projectVersion = mavenProject.getVersion();
        return projectName + "-serviceclients-" + projectVersion + ".jar";
    }
}
