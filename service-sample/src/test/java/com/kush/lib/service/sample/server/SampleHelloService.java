package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;

@Service(name = "Sample Hello Service")
public class SampleHelloService extends BaseService {

    @ServiceMethod(name = "Say Hello")
    public String sayHello(String name) {
        SampleHelloTextProvider helloTextProvider = getContext().getInstance(SampleHelloTextProvider.class);
        return helloTextProvider.getHelloText() + " " + name;
    }
}