package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.lib.service.sample.remoting.api.SampleHelloServiceApi;

public class SampleHelloServiceClient extends ServiceClient<SampleHelloServiceApi> {

    public Response<String> sayHello(String name) {
        return invoke(new ServiceTask<String>() {

            @Override
            public String execute() throws ServiceFailedException {
                return getService().sayHello(name);
            }
        });
    }

    @Override
    protected Class<SampleHelloServiceApi> getServiceApiClass() {
        return SampleHelloServiceApi.class;
    }
}
