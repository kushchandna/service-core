package com.kush.lib.service.sample;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.Response.ResultListener;
import com.kush.lib.service.client.api.ServiceClientActivationFailedException;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.sample.client.SampleConnectionSpecification;
import com.kush.lib.service.sample.client.SampleHelloServiceClient;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.sample.server.SampleHelloTextProvider;
import com.kush.lib.service.sample.server.SampleServiceNameProvider;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.ServiceInitializationFailedException;
import com.kush.lib.service.server.api.ServiceNameProvider;
import com.kush.lib.service.server.api.ServiceProvider;
import com.kush.utils.exceptions.ObjectNotFoundException;

public class SampleApplication {

    public static void main(String[] args) throws Exception {

        ServiceNameProvider serviceNameProvider = new SampleServiceNameProvider();

        ApplicationServer server = setupServer(serviceNameProvider);

        ServiceProvider serviceProvider = server.getServiceProvider();
        ConnectionSpecification connSpec = new SampleConnectionSpecification(serviceProvider, serviceNameProvider);

        ApplicationClient client = setupClient(connSpec);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeGetHelloText(serviceClientProvider);
    }

    private static ApplicationClient setupClient(ConnectionSpecification connSpec) throws ServiceClientActivationFailedException {
        Executor executor = Executors.newSingleThreadExecutor();
        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        client.activateServiceClient(SampleHelloServiceClient.class, "Sample Service", executor);
        return client;
    }

    private static ApplicationServer setupServer(ServiceNameProvider serviceNameProvider)
            throws ServiceInitializationFailedException {
        ApplicationServer server = new ApplicationServer();
        server.registerService(SampleHelloService.class);
        Context context = prepareContext(serviceNameProvider);
        server.start(context);
        return server;
    }

    private static Context prepareContext(ServiceNameProvider serviceNameProvider) {
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        return ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .withInstance(ServiceNameProvider.class, serviceNameProvider)
            .build();
    }

    private static void invokeGetHelloText(ServiceClientProvider serviceClientProvider) throws ObjectNotFoundException {
        SampleHelloServiceClient sampleServiceClient = serviceClientProvider.getServiceClient(SampleHelloServiceClient.class);
        Response<String> response = sampleServiceClient.sayHello("TestUser");
        response.setResultListener(new ResultListener<String>() {

            @Override
            public void onResult(String result) {
                System.out.println(result);
            }
        });
    }
}
