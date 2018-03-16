package com.kush.servicegen.maven;

import static com.kush.utils.commons.CollectionUtils.nonNull;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.clients.ServiceClientJarGenerator;

@Mojo(name = "generate-service-clients", defaultPhase = LifecyclePhase.COMPILE)
public class ServiceClientJarGeneratorMojo extends AbstractMojo {

    @Component
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    private File targetDirectory;
    @Parameter(defaultValue = "generated.service.client.jar.path", readonly = true)
    private String generatedServiceClientJarPath;
    @Parameter
    private List<String> services;
    @Parameter
    private List<String> additionalClasses;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String serviceClientJarName = prepareServiceClientJarName();
        ServiceClientJarGenerator jarGenerator = new ServiceClientJarGenerator(targetDirectory, serviceClientJarName);
        try {
            File jarFile = jarGenerator.generate(nonNull(services), nonNull(additionalClasses));
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
