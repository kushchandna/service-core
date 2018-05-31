package com.kush.utils.signaling.client.socket;

import java.util.concurrent.Executor;

import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.socket.SocketBasedResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;
import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.client.ClientInfo;

public class SocketClientInfo implements ClientInfo {

    private static final long serialVersionUID = 1L;

    private final String host;
    private final int port;

    public SocketClientInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ResolutionConnectionFactory getSignalSenderConnectionFactory() {
        return new SocketBasedResolutionConnectionFactory(host, port);
    }

    @Override
    public ResolutionRequestsReceiver<Signal<?>> getSignalReceiver(Executor executor) {
        return new SocketBasedResolutionRequestsProcessor<>(executor, port);
    }
}
