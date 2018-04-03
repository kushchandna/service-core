package com.kush.lib.service.remoting.connect.local;

import java.util.concurrent.BlockingQueue;

import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.receiver.ResolvableServiceRequest;

public class LocalServiceConnectionFactory implements ServiceConnectionFactory {

    private final BlockingQueue<ResolvableServiceRequest> pendingRequests;

    public LocalServiceConnectionFactory(BlockingQueue<ResolvableServiceRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public ServiceConnection createConnection() {
        return new LocalServiceConnection(pendingRequests);
    }
}
