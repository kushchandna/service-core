package com.kush.utils.remoting.connection.local;

import java.util.concurrent.BlockingQueue;

import com.kush.utils.remoting.connection.Connection;
import com.kush.utils.remoting.connection.ConnectionFactory;
import com.kush.utils.remoting.receivers.ResolvableRequest;

public class LocalConnectionFactory implements ConnectionFactory {

    private final BlockingQueue<ResolvableRequest> pendingRequests;

    public LocalConnectionFactory(BlockingQueue<ResolvableRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public Connection createConnection() {
        return new LocalConnection(pendingRequests);
    }
}
