package com.kush.utils.remoting.connection.socket;

import com.kush.utils.remoting.connection.Connection;
import com.kush.utils.remoting.connection.ConnectionFactory;
import com.kush.utils.remoting.connection.ConnectionFailedException;

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
