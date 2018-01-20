package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.api.BaseService;

public class SampleHelloService extends BaseService {

    public String sayHello(String name) {
        SampleHelloTextProvider helloTextProvider = getContext().getInstance(SampleHelloTextProvider.class);
        return helloTextProvider.getHelloText() + " " + name;
    }
}
