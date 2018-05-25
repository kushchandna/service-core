package com.kush.utils.remoting.server.local;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.ResolutionRequest;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class LocalResolutionRequestsReceiver<T extends Resolvable> extends ResolutionRequestsReceiver<T> {

    private final BlockingQueue<ResolutionRequest> pendingRequests;

    public LocalResolutionRequestsReceiver(Executor requestResolverExecutor, BlockingQueue<ResolutionRequest> pendingRequests) {
        super(requestResolverExecutor);
        this.pendingRequests = pendingRequests;
    }

    @Override
    protected void performStartup() throws StartupFailedException {
        // do nothing
    }

    @Override
    protected void performStop() throws ShutdownFailedException {
        // do nothing
    }

    @Override
    protected ResolutionRequest getNextResolvableQuery() throws ResolutionFailedException {
        try {
            return pendingRequests.take();
        } catch (InterruptedException e) {
            // TODO interrupt waiting thread
            throw new ResolutionFailedException(e);
        }
    }
}
