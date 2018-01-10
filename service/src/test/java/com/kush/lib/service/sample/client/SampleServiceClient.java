package com.kush.lib.service.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceInvoker;

public class SampleServiceClient extends ServiceClient {

    public SampleServiceClient(Executor executor, ServiceInvoker serviceInvoker) {
        super(executor, serviceInvoker, "Sample Service");
    }

    public Response<String> sayHello(String name) {
        return invoke(String.class, "sayHello", name);
    }
}
