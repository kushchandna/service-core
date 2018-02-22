package com.kush.lib.service.remoting.socket;

import com.kush.lib.service.remoting.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceRequestResolver;

public class SocketConnectionSpecification implements ConnectionSpecification {

    private final String host;
    private final int port;

    public SocketConnectionSpecification(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ServiceRequestResolver getResolver() {
        return new ClientSocketServiceRequestResolver(host, port);
    }
}
