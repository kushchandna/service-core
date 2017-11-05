package com.kush.lib.service.sample.client;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import com.kush.lib.service.api.client.Responder;
import com.kush.lib.service.api.client.Response;
import com.kush.lib.service.sample.server.SampleService;

public class SampleServiceClient {

    private final SampleService sampleService;
    private final Executor executor;

    public SampleServiceClient(SampleService sampleService, Executor executor) {
        this.sampleService = sampleService;
        this.executor = executor;
    }

    public Response<String> getHelloText(String name) {
        Responder<String> responder = new Responder<>(executor);
        return responder.invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return sampleService.getHelloText(name);
            }
        });
    }
}
