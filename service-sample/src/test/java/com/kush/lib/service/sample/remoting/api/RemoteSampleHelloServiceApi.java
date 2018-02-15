package com.kush.lib.service.sample.remoting.api;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;
import com.kush.lib.service.remoting.api.ServiceInvocationRequest;

public class RemoteSampleHelloServiceApi implements SampleHelloServiceApi {

    private final ServiceRequestResolver requestResolver;

    public RemoteSampleHelloServiceApi(ServiceRequestResolver requestResolver) {
        this.requestResolver = requestResolver;
    }

    @Override
    public String sayHello(String name) {
        ServiceInvocationRequest request = new ServiceInvocationRequest("SampleHelloService", "sayHello", name);
        return requestResolver.resolve(request, String.class);
    }
}
