package com.kush.utils.async.remoting.receivers.local;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import com.kush.utils.async.RequestFailedException;
import com.kush.utils.async.remoting.receivers.RequestReceiver;
import com.kush.utils.async.remoting.receivers.ResolvableRequest;
import com.kush.utils.async.remoting.receivers.ShutdownFailedException;
import com.kush.utils.async.remoting.receivers.StartupFailedException;

public class LocalServiceRequestReceiver extends RequestReceiver {

    private final BlockingQueue<ResolvableRequest> pendingRequests;

    public LocalServiceRequestReceiver(Executor requestResolverExecutor, BlockingQueue<ResolvableRequest> pendingRequests) {
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
    protected ResolvableRequest getNextRequest() throws RequestFailedException {
        try {
            return pendingRequests.take();
        } catch (InterruptedException e) {
            // TODO interrupt waiting thread
            throw new RequestFailedException(e);
        }
    }
}
