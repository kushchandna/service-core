package com.kush.lib.service.sample.application.local;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.local.LocalResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequest;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.local.LocalResolutionRequestsReceiver;

public class SampleLocalApplication extends SampleApplication {

    private final BlockingQueue<ResolutionRequest> pendingRequests = new ArrayBlockingQueue<>(10);

    @Override
    protected ResolutionConnectionFactory createServiceConnectionFactory() {
        return new LocalResolutionConnectionFactory(pendingRequests);
    }

    @Override
    protected ResolutionRequestsReceiver createResolutionRequestsReceiver(Executor executor) {
        return new LocalResolutionRequestsReceiver(executor, pendingRequests);
    }
}