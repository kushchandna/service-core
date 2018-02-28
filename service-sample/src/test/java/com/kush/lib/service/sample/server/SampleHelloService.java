package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.annotations.Service;
import com.kush.lib.service.server.annotations.ServiceMethod;

@Service(name = "Sample Hello Service")
public class SampleHelloService extends BaseService {

    @ServiceMethod(name = "Say Hello")
    public String sayHello(String name) {
        SampleHelloTextProvider helloTextProvider = getContext().getInstance(SampleHelloTextProvider.class);
        String textToReturn = helloTextProvider.getHelloText() + " " + name;
        System.out.println(textToReturn);
        return textToReturn;
    }
}
