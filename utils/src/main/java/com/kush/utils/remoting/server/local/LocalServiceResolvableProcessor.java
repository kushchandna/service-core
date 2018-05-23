package com.kush.utils.remoting.server.local;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.server.ResolvableProcessor;
import com.kush.utils.remoting.server.ResolvableQuery;
import com.kush.utils.remoting.server.ShutdownFailedException;
import com.kush.utils.remoting.server.StartupFailedException;

public class LocalServiceResolvableProcessor extends ResolvableProcessor {

    private final BlockingQueue<ResolvableQuery> pendingRequests;

    public LocalServiceResolvableProcessor(Executor requestResolverExecutor, BlockingQueue<ResolvableQuery> pendingRequests) {
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
    protected ResolvableQuery getNextResolvableQuery() throws ResolutionFailedException {
        try {
            return pendingRequests.take();
        } catch (InterruptedException e) {
            // TODO interrupt waiting thread
            throw new ResolutionFailedException(e);
        }
    }
}
