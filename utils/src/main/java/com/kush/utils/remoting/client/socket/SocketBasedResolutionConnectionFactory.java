package com.kush.utils.remoting.client.socket;

import com.kush.utils.remoting.client.ResolutionConnection;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.ResolutionConnectionFailedException;

public class SocketBasedResolutionConnectionFactory implements ResolutionConnectionFactory {

    private final String host;
    private final int port;

    public SocketBasedResolutionConnectionFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ResolutionConnection createConnection() throws ResolutionConnectionFailedException {
        return new SocketBasedResolutionConnection(host, port);
    }
}
