package com.kush.lib.service.sample;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.sample.client.SampleHelloServiceClient;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.sample.server.SampleHelloTextProvider;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;
import com.kush.lib.service.server.api.LocalServerBasedConnectionSpecification;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ResultListener;
import com.kush.utils.exceptions.ObjectNotFoundException;

public class SampleApplication {

    public static void main(String[] args) throws Exception {

        ApplicationServer server = new ApplicationServer();
        server.registerService(SampleHelloService.class);
        Context context = prepareContext();
        server.start(context);

        ConnectionSpecification connSpec = new LocalServerBasedConnectionSpecification(server);

        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeGetHelloText(serviceClientProvider);
    }

    private static Context prepareContext() {
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        return ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
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