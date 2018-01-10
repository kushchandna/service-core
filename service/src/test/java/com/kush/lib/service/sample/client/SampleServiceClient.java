package com.kush.lib.service.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceInvoker;
import com.kush.lib.service.client.api.ServiceTask;

public class SampleServiceClient extends ServiceClient {

    private final ServiceInvoker serviceInvoker;

    public SampleServiceClient(Executor executor, ServiceInvoker serviceInvoker) {
        super(executor);
        this.serviceInvoker = serviceInvoker;
    }

    public Response<String> getHelloText(String name) {
        return invoke(new ServiceTask<String>() {

            @Override
            public String execute() throws ServiceFailedException {
                return (String) serviceInvoker.invoke("Sample Service", "getHelloText");
            }
        });
    }
}
