package com.kush.servicegen.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    @Parameter
    private List<String> services;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        String serviceClientJarName = prepareServiceClientJarName();
        ServiceClientJarGenerator jarGenerator = new ServiceClientJarGenerator(targetDirectory, serviceClientJarName);
        try {
            jarGenerator.generate(services);
        } catch (ClassNotFoundException | CodeGenerationFailedException | IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private String prepareServiceClientJarName() {
        String projectName = mavenProject.getName();
        String projectVersion = mavenProject.getVersion();
        return projectName + "-serviceclients-" + projectVersion + ".jar";
    }
}
