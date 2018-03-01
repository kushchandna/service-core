package com.kush.lib.service.sample.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.client.api.ServiceClientProvider;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.socket.SocketServiceConnectionFactory;
import com.kush.lib.service.sample.client.SampleHelloServiceClient;
import com.kush.utils.async.Response;
import com.kush.utils.async.Response.ResultListener;

public class SampleSocketApplicationClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 3789;

    public static void main(String[] args) throws Exception {

        ServiceConnectionFactory socketReqFactory = new SocketServiceConnectionFactory(SERVER_HOST, SERVER_PORT);

        ApplicationClient client = new ApplicationClient();
        client.start(socketReqFactory);

        Executor executor = Executors.newSingleThreadExecutor();
        client.activateServiceClient(SampleHelloServiceClient.class, executor);

        ServiceClientProvider serviceClientProvider = client.getServiceClientProvider();
        invokeGetHelloText(serviceClientProvider);
    }

    private static void invokeGetHelloText(ServiceClientProvider serviceClientProvider) throws Exception {
        SampleHelloServiceClient sampleServiceClient = serviceClientProvider.getServiceClient(SampleHelloServiceClient.class);
        Response<String> response = sampleServiceClient.sayHello("TestUser");
        response.addResultListener(new ResultListener<String>() {
            @Override
            public void onResult(String result) {
                System.out.println(result);
            }
        });
    }
}
