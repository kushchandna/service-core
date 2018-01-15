package com.kush.lib.service.sample.client;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.lib.service.sample.server.api.SampleServiceApi;

public class SampleServiceClient extends ServiceClient<SampleServiceApi> {

    public SampleServiceClient() {
        super("Sample Service", SampleServiceApi.class);
    }

    public Response<String> sayHello(String name) {
        return invoke(new ServiceTask<String>() {

            @Override
            public String execute() throws ServiceFailedException {
                return getService().sayHello(name);
            }
        });
    }
}
