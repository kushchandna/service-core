package com.kush.lib.service.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.lib.service.sample.server.SampleService;

public class SampleServiceClient extends ServiceClient {

    private final SampleService sampleService;

    public SampleServiceClient(SampleService sampleService, Executor executor) {
        super(executor);
        this.sampleService = sampleService;
    }

    public Response<String> getHelloText(String name) {
        return invoke(new ServiceTask<String>() {

            @Override
            public String execute() throws ServiceFailedException {
                return sampleService.getHelloText(name);
            }
        });
    }
}
