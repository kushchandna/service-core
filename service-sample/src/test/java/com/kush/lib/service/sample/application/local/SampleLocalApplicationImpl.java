package com.kush.lib.service.sample.application.local;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.service.ApplicationServer;
import com.kush.utils.remoting.client.ConnectionFactory;
import com.kush.utils.remoting.client.local.LocalConnectionFactory;
import com.kush.utils.remoting.server.ResolvableProcessor;
import com.kush.utils.remoting.server.ResolvableQuery;
import com.kush.utils.remoting.server.local.LocalResolvableProcessor;

public class SampleLocalApplicationImpl extends SampleApplication {

    private final BlockingQueue<ResolvableQuery> pendingRequests = new ArrayBlockingQueue<>(10);

    @Override
    protected ConnectionFactory createServiceConnectionFactory() {
        return new LocalConnectionFactory(pendingRequests);
    }

    @Override
    protected void registerReceivers(ApplicationServer server) {
        Executor executor = Executors.newSingleThreadExecutor();
        ResolvableProcessor<ServiceRequest> requestReceiver = new LocalResolvableProcessor<>(executor, pendingRequests);
        server.registerServiceRequestReceiver(requestReceiver);
    }
}