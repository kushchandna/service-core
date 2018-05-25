package com.kush.lib.service.sample.application.local;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.service.ApplicationServer;
import com.kush.utils.remoting.client.ResolutionConnectionFactory;
import com.kush.utils.remoting.client.local.LocalResolutionConnectionFactory;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.ResolutionRequest;
import com.kush.utils.remoting.server.local.LocalResolutionRequestsReceiver;

public class SampleLocalApplicationImpl extends SampleApplication {

    private final BlockingQueue<ResolutionRequest> pendingRequests = new ArrayBlockingQueue<>(10);

    @Override
    protected ResolutionConnectionFactory createServiceConnectionFactory() {
        return new LocalResolutionConnectionFactory(pendingRequests);
    }

    @Override
    protected void registerReceivers(ApplicationServer server) {
        Executor executor = Executors.newSingleThreadExecutor();
        ResolutionRequestsReceiver<ServiceRequest> requestReceiver = new LocalResolutionRequestsReceiver<>(executor, pendingRequests);
        server.registerServiceRequestReceiver(requestReceiver);
    }
}