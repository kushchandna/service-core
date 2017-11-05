package com.kush.lib.service.sample.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.lib.service.sample.server.SampleApplicationServer;
import com.kush.lib.service.sample.server.SampleService;

public class SampleApplicationClient {

    private final ExecutorService executor;

    private SampleApplicationServer server;

    public SampleApplicationClient() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void init(SampleApplicationServer server) {
        this.server = server;
    }

    public void invokeGetHelloText() {
        SampleServiceClient sampleServiceClient = new SampleServiceClient(getSampleService(), executor);
        sampleServiceClient.getHelloText("TestUser");
    }

    private SampleService getSampleService() {
        return server.getService(SampleService.class);
    }
}
