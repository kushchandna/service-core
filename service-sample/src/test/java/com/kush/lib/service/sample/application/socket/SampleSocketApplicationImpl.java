package com.kush.lib.service.sample.application.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.service.ApplicationServer;
import com.kush.utils.remoting.client.ConnectionFactory;
import com.kush.utils.remoting.client.socket.SocketConnectionFactory;
import com.kush.utils.remoting.server.socket.SocketBasedResolvableProcessor;

public class SampleSocketApplicationImpl extends SampleApplication {

    private static final String HOST = "localhost";
    private static final int PORT = 3789;

    @Override
    protected ConnectionFactory createServiceConnectionFactory() {
        return new SocketConnectionFactory(HOST, PORT);
    }

    @Override
    protected void registerReceivers(ApplicationServer server) {
        Executor executor = Executors.newSingleThreadExecutor();
        server.registerServiceRequestReceiver(new SocketBasedResolvableProcessor<>(executor, PORT));
    }
}
