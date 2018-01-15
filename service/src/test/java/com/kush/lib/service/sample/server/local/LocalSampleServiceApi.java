package com.kush.lib.service.sample.server.local;

import com.kush.lib.service.sample.server.SampleService;
import com.kush.lib.service.sample.server.api.SampleServiceApi;

public class LocalSampleServiceApi implements SampleServiceApi {

    private final SampleService sampleService;

    public LocalSampleServiceApi(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @Override
    public String sayHello(String name) {
        return sampleService.sayHello(name);
    }
}
