package com.kush.lib.service.remoting.connect.local;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.receiver.ResolvableServiceRequest;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver.ResponseListener;

public class LocalServiceConnection implements ServiceConnection {

    private final BlockingQueue<ResolvableServiceRequest> pendingRequests;

    public LocalServiceConnection(BlockingQueue<ResolvableServiceRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public Object resolve(ServiceRequest request) throws ServiceRequestFailedException {
        AtomicReference<Object> resultReference = new AtomicReference<>();
        AtomicReference<ServiceRequestFailedException> exceptionReference = new AtomicReference<>();
        resolveRequestAndWaitForResult(request, resultReference, exceptionReference);
        ServiceRequestFailedException exception = exceptionReference.get();
        if (exception != null) {
            throw exception;
        }
        return resultReference.get();
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }

    private void resolveRequestAndWaitForResult(ServiceRequest request, AtomicReference<Object> resultReference,
            AtomicReference<ServiceRequestFailedException> exceptionReference) throws ServiceRequestFailedException {
        CountDownLatch latch = new CountDownLatch(1);
        ResponseListener responseListener = new LocalServiceRequestResponseListener(latch, resultReference, exceptionReference);
        ResolvableServiceRequest resolvableRequest = new ResolvableServiceRequest(request, responseListener);
        try {
            pendingRequests.put(resolvableRequest);
            latch.await();
        } catch (InterruptedException e) {
            // TODO handle
            throw new ServiceRequestFailedException(e);
        }
    }

    private final class LocalServiceRequestResponseListener implements ResponseListener {

        private final CountDownLatch latch;
        private final AtomicReference<Object> resultReference;
        private final AtomicReference<ServiceRequestFailedException> exceptionReference;

        public LocalServiceRequestResponseListener(CountDownLatch latch, AtomicReference<Object> resultReference,
                AtomicReference<ServiceRequestFailedException> exceptionReference) {
            this.latch = latch;
            this.resultReference = resultReference;
            this.exceptionReference = exceptionReference;
        }

        @Override
        public void onResult(Object result) {
            resultReference.set(result);
            responseReceived();
        }

        @Override
        public void onError(ServiceRequestFailedException e) {
            exceptionReference.set(e);
            responseReceived();
        }

        private void responseReceived() {
            latch.countDown();
        }
    }
}
