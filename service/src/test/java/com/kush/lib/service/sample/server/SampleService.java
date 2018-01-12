package com.kush.lib.service.sample.server;

import com.kush.lib.service.sample.api.SampleServiceApi;
import com.kush.lib.service.server.api.BaseService;

public class SampleService extends BaseService implements SampleServiceApi {

    @Override
    public String sayHello(String name) {
        SampleGreetingProvider greetingProvider = getContext().getInstance(SampleGreetingProvider.class);
        return greetingProvider.getHelloGreeting() + " " + name;
    }
}
