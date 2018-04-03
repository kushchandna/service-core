package com.kush.lib.service.remoting.receiver.local;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ResolvableServiceRequest;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;

public class LocalServiceRequestReceiver extends ServiceRequestReceiver {

    private final BlockingQueue<ResolvableServiceRequest> pendingRequests;

    public LocalServiceRequestReceiver(Executor requestResolverExecutor,
            BlockingQueue<ResolvableServiceRequest> pendingRequests) {
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
    protected ResolvableServiceRequest getNextRequest() throws ServiceRequestFailedException {
        try {
            return pendingRequests.take();
        } catch (InterruptedException e) {
            // TODO interrupt waiting thread
            throw new ServiceRequestFailedException(e.getMessage(), e);
        }
    }
}
