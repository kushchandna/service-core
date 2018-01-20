package com.kush.lib.service.sample.remoting.local;

import com.kush.lib.service.sample.remoting.api.SampleHelloServiceApi;
import com.kush.lib.service.sample.server.SampleHelloService;

public class SampleLocalHelloServiceApi implements SampleHelloServiceApi {

    private final SampleHelloService service;

    public SampleLocalHelloServiceApi(SampleHelloService service) {
        this.service = service;
    }

    @Override
    public String sayHello(String name) {
        return service.sayHello(name);
    }
}
