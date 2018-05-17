package com.kush.lib.service.sample.application.socket;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.socket.SocketServiceConnectionFactory;
import com.kush.lib.service.remoting.receiver.socket.ServerSocketServiceRequestReceiver;
import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.service.ApplicationServer;

public class SampleSocketApplicationImpl extends SampleApplication {

    private static final String HOST = "localhost";
    private static final int PORT = 3789;

    @Override
    protected ServiceConnectionFactory createServiceConnectionFactory() {
        return new SocketServiceConnectionFactory(HOST, PORT);
    }

    @Override
    protected void registerReceivers(ApplicationServer server) {
        Executor executor = Executors.newSingleThreadExecutor();
        server.registerServiceRequestReceiver(new ServerSocketServiceRequestReceiver(executor, PORT));
    }
}
