package com.kush.servicegen.maven;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.kush.servicegen.CodeGenerationFailedException;
import com.kush.servicegen.clients.ServiceClientInfo;
import com.kush.servicegen.clients.ServiceClientJarGenerator;

@Mojo(name = "service-client-generator", defaultPhase = LifecyclePhase.COMPILE)
public class ServiceClientJarGeneratorMojo extends AbstractMojo {

    @Parameter
    private File targetDirectory;
    @Parameter
    private String targetJarName;
    @Parameter
    private ServiceClientInfo[] serviceClients;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ServiceClientJarGenerator jarGenerator = new ServiceClientJarGenerator(targetDirectory, targetJarName);
        Collection<ServiceClientInfo> serviceClientInfos = Arrays.asList(serviceClients);
        try {
            jarGenerator.generate(serviceClientInfos);
        } catch (ClassNotFoundException | CodeGenerationFailedException | IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
