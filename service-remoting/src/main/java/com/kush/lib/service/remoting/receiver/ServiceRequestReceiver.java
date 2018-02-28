package com.kush.lib.service.remoting.receiver;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;

public abstract class ServiceRequestReceiver {

    private final Executor executor;

    private volatile boolean running = false;

    public ServiceRequestReceiver(Executor executor) {
        this.executor = executor;
    }

    public final void start(ServiceRequestResolver requestResolver) throws StartupFailedException {
        performStartup();
        running = true;
        startProcessingRequests(requestResolver);
    }

    public final void stop() throws ShutdownFailedException {
        performStop();
        running = false;
    }

    private void startProcessingRequests(ServiceRequestResolver requestResolver) {
        while (running) {
            ResolvableServiceRequest resolvableRequest;
            try {
                resolvableRequest = getNextRequest();
            } catch (ServiceRequestFailedException e) {
                // TODO add error handling
                continue;
            }
            executor.execute(new Task(requestResolver, resolvableRequest));
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
            try {
                Object result = requestResolver.resolve(resolvableRequest.getRequest());
                resolvableRequest.getListener().onResult(result);
            } catch (ServiceRequestFailedException e) {
                resolvableRequest.getListener().onError(e);
            }
        }
    }

    protected static interface ResponseListener {

        void onResult(Object result);

        void onError(ServiceRequestFailedException e);
    }
}
