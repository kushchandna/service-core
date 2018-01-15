package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.annotations.Service;

@Service(name = "Sample Service")
public class SampleService extends BaseService {

    public String sayHello(String name) {
        SampleGreetingProvider greetingProvider = getContext().getInstance(SampleGreetingProvider.class);
        return greetingProvider.getHelloGreeting() + " " + name;
    }
}
