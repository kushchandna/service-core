package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.sample.remoting.api.SampleHelloServiceApi;
import com.kush.utils.async.Response;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.Request;

public class SampleHelloServiceClient extends ServiceClient<SampleHelloServiceApi> {

    public Response<String> sayHello(String name) {
        return invoke(new Request<String>() {

            @Override
            public String process() throws RequestFailedException {
                return getService().sayHello(name);
            }
        });
    }

    @Override
    protected Class<SampleHelloServiceApi> getServiceApiClass() {
        return SampleHelloServiceApi.class;
    }
}
