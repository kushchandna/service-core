package com.kush.lib.service.sample.client;

import java.util.concurrent.Callable;

import com.kush.lib.service.api.client.Responder;
import com.kush.lib.service.api.client.Response;
import com.kush.lib.service.sample.server.SampleService;

public class SampleServiceClient {

    private final SampleService sampleService;
    private final Responder responder;

    public SampleServiceClient(SampleService sampleService, Responder responder) {
        this.sampleService = sampleService;
        this.responder = responder;
    }

    public Response<String> getHelloText(String name) {
        return responder.invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return sampleService.getHelloText(name);
            }
        });
    }
}
