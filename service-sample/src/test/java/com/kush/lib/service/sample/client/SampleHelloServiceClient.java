package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.utils.async.Response;

public class SampleHelloServiceClient extends ServiceClient {

    public Response<String> sayHello(String name) {
        return invoke("sayHello", String.class, name);
    }
}
