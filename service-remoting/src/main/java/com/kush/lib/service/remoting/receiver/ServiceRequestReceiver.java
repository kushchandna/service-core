package com.kush.lib.service.remoting.receiver;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;

public abstract class ServiceRequestReceiver<T extends ServiceRequestProvider> {

    private final ServiceRequestResolver requestResolver;
    private final Executor executor;

    private volatile boolean running = false;

    public ServiceRequestReceiver(ServiceRequestResolver requestResolver, Executor executor) {
        this.requestResolver = requestResolver;
        this.executor = executor;
    }

    public final void start() throws StartupFailedException {
        performStartup();
        running = true;
        startProcessingRequests();
    }

    public final void stop() throws ShutdownFailedException {
        performStop();
        running = false;
    }

    private void startProcessingRequests() {
        while (running) {
            T requestProvider;
            try {
                requestProvider = getNextRequest();
                executor.execute(new Task(requestProvider));
            } catch (ServiceRequestFailedException e) {
                sendError(e);
            }
        }
    }

    protected abstract void performStartup() throws StartupFailedException;

    protected abstract void performStop() throws ShutdownFailedException;

    protected abstract T getNextRequest() throws ServiceRequestFailedException;

    protected abstract void sendResult(T requestProvider, Object result) throws ServiceRequestFailedException;

    protected abstract void sendError(ServiceRequestFailedException e);

    private final class Task implements Runnable {

        private final T requestProvider;

        public Task(T requestProvider) {
            this.requestProvider = requestProvider;
        }

        @Override
        public void run() {
            try {
                ServiceRequest<?> request = requestProvider.getServiceRequest();
                Object result = requestResolver.resolve(request);
                sendResult(requestProvider, result);
            } catch (ServiceRequestFailedException e) {
                sendError(e);
            }
        }
    }
}
