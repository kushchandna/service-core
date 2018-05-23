package com.kush.utils.remoting.connection.local;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;
import com.kush.utils.remoting.connection.Connection;
import com.kush.utils.remoting.receivers.ResolvableRequest;
import com.kush.utils.remoting.receivers.RequestReceiver.ResponseListener;

public class LocalConnection implements Connection {

    private final BlockingQueue<ResolvableRequest> pendingRequests;

    public LocalConnection(BlockingQueue<ResolvableRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public Object resolve(Request<?> request) throws RequestFailedException {
        AtomicReference<Object> resultReference = new AtomicReference<>();
        AtomicReference<RequestFailedException> exceptionReference = new AtomicReference<>();
        resolveRequestAndWaitForResult(request, resultReference, exceptionReference);
        RequestFailedException exception = exceptionReference.get();
        if (exception != null) {
            throw exception;
        }
        return resultReference.get();
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }

    private void resolveRequestAndWaitForResult(Request<?> request, AtomicReference<Object> resultReference,
            AtomicReference<RequestFailedException> exceptionReference) throws RequestFailedException {
        CountDownLatch latch = new CountDownLatch(1);
        ResponseListener responseListener = new LocalRequestResponseListener(latch, resultReference, exceptionReference);
        ResolvableRequest resolvableRequest = new ResolvableRequest(request, responseListener);
        try {
            pendingRequests.put(resolvableRequest);
            latch.await();
        } catch (InterruptedException e) {
            // TODO handle
            throw new RequestFailedException(e);
        }
    }

    private final class LocalRequestResponseListener implements ResponseListener {

        private final CountDownLatch latch;
        private final AtomicReference<Object> resultReference;
        private final AtomicReference<RequestFailedException> exceptionReference;

        public LocalRequestResponseListener(CountDownLatch latch, AtomicReference<Object> resultReference,
                AtomicReference<RequestFailedException> exceptionReference) {
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
        public void onError(RequestFailedException e) {
            exceptionReference.set(e);
            responseReceived();
        }

        private void responseReceived() {
            latch.countDown();
        }
    }
}
