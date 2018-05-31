package com.kush.lib.service.sample.application.socket;

import java.util.concurrent.Executor;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.socket.SocketBasedResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;

public class SampleSocketApplicationImpl extends SampleApplication {

    private static final String HOST = "localhost";
    private static final int PORT = 3789;

    @Override
    protected ResolutionConnectionFactory createServiceConnectionFactory() {
        return new SocketBasedResolutionConnectionFactory(HOST, PORT);
    }

    @Override
    protected ResolutionRequestsReceiver createResolutionRequestsReceiver(Executor executor) {
        return new SocketBasedResolutionRequestsProcessor(executor, PORT);
    }
}
