package com.kush.utils.remoting.client.local;

import java.util.concurrent.BlockingQueue;

import com.kush.utils.remoting.client.Connection;
import com.kush.utils.remoting.client.ConnectionFactory;
import com.kush.utils.remoting.server.ResolvableQuery;

public class LocalConnectionFactory implements ConnectionFactory {

    private final BlockingQueue<ResolvableQuery> pendingRequests;

    public LocalConnectionFactory(BlockingQueue<ResolvableQuery> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public Connection createConnection() {
        return new LocalConnection(pendingRequests);
    }
}
