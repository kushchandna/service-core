package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.api.BaseService;
import com.kush.lib.service.server.api.annotations.Service;
import com.kush.lib.service.server.api.annotations.ServiceMethod;

@Service(name = "Sample Service")
public class SampleService extends BaseService {

    @ServiceMethod
    public String sayHello(String name) {
        SampleGreetingProvider greetingProvider = getContext().getInstance(SampleGreetingProvider.class);
        return greetingProvider.getHelloGreeting() + " " + name;
    }
}
