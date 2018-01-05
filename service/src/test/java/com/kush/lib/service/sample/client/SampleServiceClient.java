package com.kush.lib.service.sample.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.sample.server.SampleService;

public class SampleServiceClient extends ServiceClient {

    private final SampleService sampleService;

    public SampleServiceClient(SampleService sampleService, Executor executor) {
        super(executor);
        this.sampleService = sampleService;
    }

    public Response<String> getHelloText(String name) {
        return invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return sampleService.getHelloText(name);
            }
        });
    }
}
