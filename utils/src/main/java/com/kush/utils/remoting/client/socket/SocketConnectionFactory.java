package com.kush.utils.remoting.client.socket;

import com.kush.utils.remoting.client.Connection;
import com.kush.utils.remoting.client.ConnectionFactory;
import com.kush.utils.remoting.client.ConnectionFailedException;

public class SocketConnectionFactory implements ConnectionFactory {

    private final String host;
    private final int port;

    public SocketConnectionFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Connection createConnection() throws ConnectionFailedException {
        return new SocketConnection(host, port);
    }
}
