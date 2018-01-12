package com.kush.lib.service.sample.api;

import com.kush.lib.service.remoting.ServiceApi;

public interface SampleServiceApi extends ServiceApi {

    String sayHello(String name);

}