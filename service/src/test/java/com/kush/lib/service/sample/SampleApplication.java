package com.kush.lib.service.sample;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.Response.ResultListener;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.sample.client.SampleConnectionSpecification;
import com.kush.lib.service.sample.client.SampleServiceClient;
import com.kush.lib.service.sample.server.SampleGreetingProvider;
import com.kush.lib.service.sample.server.SampleService;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleApplication {

    public static void main(String[] args) {
        SampleGreetingProvider greetingProvider = new SampleGreetingProvider();
        Context context = ContextBuilder.create()
            .withInstance(SampleGreetingProvider.class, greetingProvider)
            .build();
        ApplicationServer server = new ApplicationServer(context);
        server.registerService(SampleService.class);
        server.start();

        ServiceProvider serviceProvider = server.getServiceProvider();
        ConnectionSpecification connSpec = new SampleConnectionSpecification(serviceProvider);

        Executor executor = Executors.newSingleThreadExecutor();
        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        client.activateServiceClient(SampleServiceClient.class, executor);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeGetHelloText(serviceClientProvider);
    }

    private static void invokeGetHelloText(ServiceClientProvider serviceClientProvider) {
        SampleServiceClient sampleServiceClient = serviceClientProvider.getServiceClient(SampleServiceClient.class);
        Response<String> response = sampleServiceClient.getHelloText("TestUser");
        response.setResultListener(new ResultListener<String>() {

            @Override
            public void onResult(String result) {
                System.out.println(result);
            }
        });
    }
}
