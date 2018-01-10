package com.kush.lib.service.sample.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.Response.ResultListener;
import com.kush.lib.service.sample.server.SampleService;
import com.kush.lib.service.server.api.ServiceProvider;

public class SampleApplicationClient {

    private final ExecutorService executor;

    private ServiceProvider serviceProvider;

    public SampleApplicationClient() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void init(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void invokeGetHelloText() {
        SampleServiceClient sampleServiceClient = new SampleServiceClient(getSampleService(), executor);
        Response<String> response = sampleServiceClient.getHelloText("TestUser");
        response.setResultListener(new ResultListener<String>() {

            @Override
            public void onResult(String result) {
                System.out.println(result);
            }
        });
    }

    private SampleService getSampleService() {
        return serviceProvider.getService(SampleService.class);
    }
}
