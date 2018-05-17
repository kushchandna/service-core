package com.kush.lib.service.sample.application.local;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.service.remoting.receiver.ResolvableServiceRequest;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;
import com.kush.lib.service.remoting.receiver.local.LocalServiceRequestReceiver;
import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.service.ApplicationServer;

public class SampleLocalApplicationImpl extends SampleApplication {

    private final BlockingQueue<ResolvableServiceRequest> pendingRequests = new ArrayBlockingQueue<>(10);

    @Override
    protected ServiceConnectionFactory createServiceConnectionFactory() {
        return new LocalServiceConnectionFactory(pendingRequests);
    }

    @Override
    protected void registerReceivers(ApplicationServer server) {
        Executor executor = Executors.newSingleThreadExecutor();
        ServiceRequestReceiver requestReceiver = new LocalServiceRequestReceiver(executor, pendingRequests);
        server.registerServiceRequestReceiver(requestReceiver);
    }
}