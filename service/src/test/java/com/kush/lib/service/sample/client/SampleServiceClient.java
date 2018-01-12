package com.kush.lib.service.sample.client;

import java.util.concurrent.Executor;

import com.kush.lib.service.client.api.Response;
import com.kush.lib.service.client.api.ServiceClient;
import com.kush.lib.service.client.api.ServiceFailedException;
import com.kush.lib.service.client.api.ServiceTask;
import com.kush.lib.service.sample.api.SampleServiceApi;

public class SampleServiceClient extends ServiceClient<SampleServiceApi> {

    public SampleServiceClient(Executor executor, SampleServiceApi serviceApi) {
        super(executor, serviceApi);
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
