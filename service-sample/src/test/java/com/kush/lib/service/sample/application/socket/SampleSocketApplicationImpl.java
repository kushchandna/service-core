package com.kush.lib.service.sample.application.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.service.ApplicationServer;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.socket.SocketBasedResolutionConnectionFactory;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;

public class SampleSocketApplicationImpl extends SampleApplication {

    private static final String HOST = "localhost";
    private static final int PORT = 3789;

    @Override
    protected ResolutionConnectionFactory createServiceConnectionFactory() {
        return new SocketBasedResolutionConnectionFactory(HOST, PORT);
    }

    @Override
    protected void registerReceivers(ApplicationServer server) {
        Executor executor = Executors.newSingleThreadExecutor();
        server.registerServiceRequestReceiver(new SocketBasedResolutionRequestsProcessor(executor, PORT));
    }
}
