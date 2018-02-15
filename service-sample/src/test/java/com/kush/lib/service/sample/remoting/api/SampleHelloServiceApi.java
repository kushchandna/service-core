package com.kush.lib.service.sample.remoting.api;

import com.kush.lib.service.remoting.api.ServiceApi;

public interface SampleHelloServiceApi extends ServiceApi {

    String sayHello(String name);
}
