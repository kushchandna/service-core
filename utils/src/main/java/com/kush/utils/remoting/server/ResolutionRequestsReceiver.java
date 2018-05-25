package com.kush.utils.remoting.server;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;

public abstract class ResolutionRequestsReceiver<T extends Resolvable> {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ResolutionRequestsReceiver.class);

    private final Executor resolutionExecutor;
    private final ExecutorService resolvableReceiverExecutor;

    private volatile boolean running = false;

    public ResolutionRequestsReceiver(Executor requestResolverExecutor) {
        resolutionExecutor = requestResolverExecutor;
        resolvableReceiverExecutor = Executors.newSingleThreadExecutor();
    }

    public final void start(Resolver<T> resolver) throws StartupFailedException {
        LOGGER.info("Starting request receiver %s", getClass().getName());
        performStartup();
        running = true;
        LOGGER.info("Started request receiver %s", getClass().getName());
        resolvableReceiverExecutor.execute(new Runnable() {

            @Override
            public void run() {
                startProcessingRequests(resolver);
            }
        });
    }

    public final void stop() throws ShutdownFailedException {
        LOGGER.info("Stopping request receiver %s", getClass().getName());
        performStop();
        resolvableReceiverExecutor.shutdownNow();
        running = false;
        LOGGER.info("Stopped request receiver %s", getClass().getName());
    }

    private void startProcessingRequests(Resolver<T> resolver) {
        while (running) {
            ResolutionRequest resolvableQuery;
            try {
                resolvableQuery = getNextResolvableQuery();
            } catch (ResolutionFailedException e) {
                // TODO add error handling
                continue;
            }
            resolutionExecutor.execute(new Task<T>(resolvableQuery, resolver));
        }
    }

    protected abstract void performStartup() throws StartupFailedException;

    protected abstract void performStop() throws ShutdownFailedException;

    protected abstract ResolutionRequest getNextResolvableQuery() throws ResolutionFailedException;

    private static final class Task<T extends Resolvable> implements Runnable {

        private final ResolutionRequest resolvableQuery;
        private final Resolver<T> resolver;

        public Task(ResolutionRequest resolvableQuery, Resolver<T> resolver) {
            this.resolvableQuery = resolvableQuery;
            this.resolver = resolver;
        }

        @Override
        public void run() {
            Resolvable resolvable = resolvableQuery.getResolvable();
            try {
                @SuppressWarnings("unchecked")
                Object result = resolver.resolve((T) resolvable);
                resolvableQuery.getListener().onResult(result);
            } catch (Exception e) {
                LOGGER.error(e);
                resolvableQuery.getListener().onError(new ResolutionFailedException(e));
            }
        }
    }

    public static interface ResponseListener {

        void onResult(Object result);

        void onError(ResolutionFailedException e);
    }
}
