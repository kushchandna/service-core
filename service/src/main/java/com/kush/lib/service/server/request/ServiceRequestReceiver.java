package com.kush.lib.service.server.request;

import java.util.concurrent.Executor;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;

public abstract class ServiceRequestReceiver implements Runnable {

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
    }

    public final void stop() {
        performStop();
        running = false;
    }

    @Override
    public final void run() {
        while (running) {
            ServiceRequest<?> request;
            try {
                request = getNextRequest();
                executor.execute(new Task(request));
            } catch (ServiceRequestFailedException e) {
                sendError(e);
            }
        }
    }

    protected abstract void sendError(ServiceRequestFailedException e);

    protected abstract void sendResult(ServiceRequest<?> request, Object result);

    protected abstract ServiceRequest<?> getNextRequest() throws ServiceRequestFailedException;

    protected abstract void performStartup() throws StartupFailedException;

    protected abstract void performStop();

    private final class Task implements Runnable {

        private final ServiceRequest<?> request;

        public Task(ServiceRequest<?> request) {
            this.request = request;
        }

        @Override
        public void run() {
            try {
                Object result = requestResolver.resolve(request);
                sendResult(request, result);
            } catch (ServiceRequestFailedException e) {
                sendError(e);
            }
        }
    }
}
