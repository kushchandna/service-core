package com.kush.lib.service.remoting.connect.socket;

import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.ServiceConnectionFailedException;

public class SocketServiceConnectionFactory implements ServiceConnectionFactory {

    private final SocketServiceConnectionSpecification connSpec;

    public SocketServiceConnectionFactory(SocketServiceConnectionSpecification connSpec) {
        this.connSpec = connSpec;
    }

    @Override
    public ServiceConnection createConnection() throws ServiceConnectionFailedException {
        String host = connSpec.getHost();
        int port = connSpec.getPort();
        return new SocketServiceConnection(host, port);
    }
}
