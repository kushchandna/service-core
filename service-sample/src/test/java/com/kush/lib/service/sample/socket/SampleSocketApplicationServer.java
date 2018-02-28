package com.kush.lib.service.sample.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;
import com.kush.lib.service.remoting.receiver.socket.ServerSocketServiceRequestReceiver;
import com.kush.lib.service.sample.server.SampleHelloService;
import com.kush.lib.service.sample.server.SampleHelloTextProvider;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;

public class SampleSocketApplicationServer {

    private static final int REQUEST_PORT = 3789;

    public static void main(String[] args) throws Exception {

        ApplicationServer server = new ApplicationServer();

        Executor executor = Executors.newSingleThreadExecutor();
        ServiceRequestReceiver socketReqReceiver = new ServerSocketServiceRequestReceiver(executor, REQUEST_PORT);
        server.registerServiceRequestReceiver(socketReqReceiver);

        server.registerService(SampleHelloService.class);

        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        Context context = ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .build();

        server.start(context);
    }
}
