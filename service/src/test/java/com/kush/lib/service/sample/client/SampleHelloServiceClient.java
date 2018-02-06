package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.sample.remoting.api.SampleHelloServiceApi;
import com.kush.utils.async.Response;
import com.kush.utils.async.ServiceFailedException;
import com.kush.utils.async.ServiceTask;

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
