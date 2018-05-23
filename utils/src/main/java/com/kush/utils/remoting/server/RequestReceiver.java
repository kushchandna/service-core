package com.kush.utils.remoting.server;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.utils.async.Request;
import com.kush.utils.async.RequestFailedException;

public abstract class RequestReceiver {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(RequestReceiver.class);

    private final Executor requestResolverExecutor;
    private final ExecutorService requestReceiverExecutor;

    private volatile boolean running = false;

    public RequestReceiver(Executor requestResolverExecutor) {
        this.requestResolverExecutor = requestResolverExecutor;
        requestReceiverExecutor = Executors.newSingleThreadExecutor();
    }

    public final void start() throws StartupFailedException {
        LOGGER.info("Starting request receiver %s", getClass().getName());
        performStartup();
        running = true;
        LOGGER.info("Started request receiver %s", getClass().getName());
        requestReceiverExecutor.execute(new Runnable() {

            @Override
            public void run() {
                startProcessingRequests();
            }
        });
    }

    public final void stop() throws ShutdownFailedException {
        LOGGER.info("Stopping request receiver %s", getClass().getName());
        performStop();
        requestReceiverExecutor.shutdownNow();
        running = false;
        LOGGER.info("Stopped request receiver %s", getClass().getName());
    }

    private void startProcessingRequests() {
        while (running) {
            ResolvableRequest resolvableRequest;
            try {
                resolvableRequest = getNextRequest();
            } catch (RequestFailedException e) {
                // TODO add error handling
                continue;
            }
            requestResolverExecutor.execute(new Task(resolvableRequest));
        }
    }

    protected abstract void performStartup() throws StartupFailedException;

    protected abstract void performStop() throws ShutdownFailedException;

    protected abstract ResolvableRequest getNextRequest() throws RequestFailedException;

    private static final class Task implements Runnable {

        private final ResolvableRequest resolvableRequest;

        public Task(ResolvableRequest resolvableRequest) {
            this.resolvableRequest = resolvableRequest;
        }

        @Override
        public void run() {
            Request<?> request = resolvableRequest.getRequest();
            try {
                Object result = request.process();
                resolvableRequest.getListener().onResult(result);
            } catch (Exception e) {
                LOGGER.error(e);
                resolvableRequest.getListener().onError(new RequestFailedException(e));
            }
        }
    }

    public static interface ResponseListener {

        void onResult(Object result);

        void onError(RequestFailedException e);
    }
}
