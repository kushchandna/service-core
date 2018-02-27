package com.kush.lib.service.sample;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.service.sample.client.SampleHelloServiceClient;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.sample.server.SampleHelloTextProvider;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.ServiceInitializationFailedException;
import com.kush.lib.service.server.local.LocalApplicationServer;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ResultListener;

public class SampleApplication {

    public static void main(String[] args) throws Exception {
        setupServer();
        ServiceConnectionFactory connFactory = new LocalServiceConnectionFactory();
        ApplicationClient client = setupClient(connFactory);
        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeGetHelloText(serviceClientProvider);
    }

    private static void setupServer() throws ServiceInitializationFailedException {
        ApplicationServer server = new LocalApplicationServer();
        server.registerService(SampleHelloService.class);
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        Context context = ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .build();
        server.start(context);
    }

    private static ApplicationClient setupClient(ServiceConnectionFactory connFactory) throws Exception {
        ApplicationClient client = new ApplicationClient();
        client.start(connFactory);
        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);
        return client;
    }

    private static void invokeGetHelloText(ServiceClientProvider serviceClientProvider) throws Exception {
        SampleHelloServiceClient sampleServiceClient = serviceClientProvider.getServiceClient(SampleHelloServiceClient.class);
        Response<String> response = sampleServiceClient.sayHello("TestUser");
        System.out.println(response.getResult());
        response.setResultListener(new ResultListener<String>() {
            @Override
            public void onResult(String result) {
                System.out.println(result);
            }
        });
    }
}
