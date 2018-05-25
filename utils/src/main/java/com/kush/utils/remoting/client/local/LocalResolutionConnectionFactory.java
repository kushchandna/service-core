package com.kush.utils.remoting.client.local;

import java.util.concurrent.BlockingQueue;

import com.kush.utils.remoting.client.ResolutionConnection;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequest;

public class LocalResolutionConnectionFactory implements ResolutionConnectionFactory {

    private final BlockingQueue<ResolutionRequest> pendingRequests;

    public LocalResolutionConnectionFactory(BlockingQueue<ResolutionRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public ResolutionConnection createConnection() {
        return new LocalResolutionConnection(pendingRequests);
    }
}
