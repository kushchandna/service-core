package com.kush.utils.remoting.client.local;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;
import com.kush.utils.remoting.client.ResolutionConnection;
import com.kush.utils.remoting.server.ResolutionRequest;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver.ResponseListener;

class LocalResolutionConnection implements ResolutionConnection {

    private final BlockingQueue<ResolutionRequest> pendingRequests;

    public LocalResolutionConnection(BlockingQueue<ResolutionRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    @Override
    public Object resolve(Resolvable resolvable) throws ResolutionFailedException {
        AtomicReference<Object> resultReference = new AtomicReference<>();
        AtomicReference<ResolutionFailedException> exceptionReference = new AtomicReference<>();
        resolveAndWaitForResult(resolvable, resultReference, exceptionReference);
        ResolutionFailedException exception = exceptionReference.get();
        if (exception != null) {
            throw exception;
        }
        return resultReference.get();
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }

    private void resolveAndWaitForResult(Resolvable resolvable, AtomicReference<Object> resultReference,
            AtomicReference<ResolutionFailedException> exceptionReference) throws ResolutionFailedException {
        CountDownLatch latch = new CountDownLatch(1);
        ResponseListener responseListener = new LocalResolutionResponseListener(latch, resultReference, exceptionReference);
        ResolutionRequest resolvableRequest = new ResolutionRequest(resolvable, responseListener);
        try {
            pendingRequests.put(resolvableRequest);
            latch.await();
        } catch (InterruptedException e) {
            // TODO handle
            throw new ResolutionFailedException(e);
        }
    }

    private final class LocalResolutionResponseListener implements ResponseListener {

        private final CountDownLatch latch;
        private final AtomicReference<Object> resultReference;
        private final AtomicReference<ResolutionFailedException> exceptionReference;

        public LocalResolutionResponseListener(CountDownLatch latch, AtomicReference<Object> resultReference,
                AtomicReference<ResolutionFailedException> exceptionReference) {
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
        public void onError(ResolutionFailedException e) {
            exceptionReference.set(e);
            responseReceived();
        }

        private void responseReceived() {
            latch.countDown();
        }
    }
}
