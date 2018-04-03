package com.kush.lib.service.remoting.receiver;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;

public abstract class ServiceRequestReceiver {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ServiceRequestReceiver.class);

    private final Executor requestResolverExecutor;
    private final ExecutorService requestReceiverExecutor;

    private volatile boolean running = false;

    public ServiceRequestReceiver(Executor requestResolverExecutor) {
        this.requestResolverExecutor = requestResolverExecutor;
        requestReceiverExecutor = Executors.newSingleThreadExecutor();
    }

    public final void start(ServiceRequestResolver requestResolver) throws StartupFailedException {
        LOGGER.info("Starting request receiver %s", getClass().getName());
        performStartup();
        running = true;
        LOGGER.info("Started request receiver %s", getClass().getName());
        requestReceiverExecutor.execute(new Runnable() {

            @Override
            public void run() {
                startProcessingRequests(requestResolver);
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

    private void startProcessingRequests(ServiceRequestResolver requestResolver) {
        while (running) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Waiting for next request [Thread: %s]", Thread.currentThread().getName());
            }
            ResolvableServiceRequest resolvableRequest;
            try {
                resolvableRequest = getNextRequest();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Received request %s [Thread: %s]", resolvableRequest.getRequest(),
                            Thread.currentThread().getName());
                }
            } catch (ServiceRequestFailedException e) {
                // TODO add error handling
                continue;
            }
            requestResolverExecutor.execute(new Task(requestResolver, resolvableRequest));
        }
    }

    protected abstract void performStartup() throws StartupFailedException;

    protected abstract void performStop() throws ShutdownFailedException;

    protected abstract ResolvableServiceRequest getNextRequest() throws ServiceRequestFailedException;

    private static final class Task implements Runnable {

        private final ServiceRequestResolver requestResolver;
        private final ResolvableServiceRequest resolvableRequest;

        public Task(ServiceRequestResolver requestResolver, ResolvableServiceRequest resolvableRequest) {
            this.requestResolver = requestResolver;
            this.resolvableRequest = resolvableRequest;
        }

        @Override
        public void run() {
            ServiceRequest request = resolvableRequest.getRequest();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Resolving request %s", request);
            }
            try {
                Object result = requestResolver.resolve(request);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Received %s for request %s from service %s", result, request.getMethodName(),
                            request.getServiceName());
                }
                resolvableRequest.getListener().onResult(result);
            } catch (ServiceRequestFailedException e) {
                LOGGER.error("Error occured with message %s while resolving request %s from service %s", e.getMessage(),
                        request.getMethodName(), request.getServiceName());
                resolvableRequest.getListener().onError(e);
            }
        }
    }

    public static interface ResponseListener {

        void onResult(Object result);

        void onError(ServiceRequestFailedException e);
    }
}
