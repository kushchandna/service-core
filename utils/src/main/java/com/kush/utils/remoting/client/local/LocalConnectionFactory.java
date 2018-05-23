package com.kush.utils.remoting.client.local;

import java.util.concurrent.BlockingQueue;

import com.kush.utils.remoting.client.Connection;
import com.kush.utils.remoting.client.ConnectionFactory;
import com.kush.utils.remoting.server.ResolvableRequest;

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
