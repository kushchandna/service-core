package com.kush.lib.service.sample.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.remoting.ConnectionSpecification;
import com.kush.lib.service.remoting.local.LocalServerBasedConnectionSpecification;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ResultListener;
import com.kush.utils.exceptions.ObjectNotFoundException;

public class SampleClientMain {

    public static void main(String[] args) throws Exception {
        ConnectionSpecification connSpec = new LocalServerBasedConnectionSpecification();

        ApplicationClient client = new ApplicationClient();
        client.connect(connSpec);
        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeGetHelloText(serviceClientProvider);
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
