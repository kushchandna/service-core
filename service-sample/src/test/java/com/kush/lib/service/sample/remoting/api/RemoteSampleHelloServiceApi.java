package com.kush.lib.service.sample.remoting.api;

import com.kush.lib.service.remoting.api.RequestResolver;
import com.kush.lib.service.remoting.api.ServiceInvocationRequest;

public class RemoteSampleHelloServiceApi implements SampleHelloServiceApi {

    private final RequestResolver requestResolver;

    public RemoteSampleHelloServiceApi(RequestResolver requestResolver) {
        this.requestResolver = requestResolver;
    }

    @Override
    public String sayHello(String name) {
        ServiceInvocationRequest request = new ServiceInvocationRequest("SampleHelloService", "sayHello", name);
        return requestResolver.resolve(request, String.class);
    }
}
