package com.kush.lib.service.sample.server;

import com.kush.lib.service.api.server.Service;

public class SampleService extends Service {

    public String getHelloText(String name) {
        return "Hello " + name;
    }
}
