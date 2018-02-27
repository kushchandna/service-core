package com.kush.lib.service.remoting.connect.socket;

import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.ServiceConnectionFailedException;

public class SocketServiceConnectionFactory implements ServiceConnectionFactory {

    private final String host;
    private final int port;

    public SocketServiceConnectionFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public ServiceConnection createConnection() throws ServiceConnectionFailedException {
        return new SocketServiceConnection(host, port);
    }
}
