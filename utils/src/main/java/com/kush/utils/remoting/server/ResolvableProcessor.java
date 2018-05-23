package com.kush.utils.remoting.server;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;

public abstract class ResolvableProcessor {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ResolvableProcessor.class);

    private final Executor resolutionExecutor;
    private final ExecutorService resolvableReceiverExecutor;

    private volatile boolean running = false;

    public ResolvableProcessor(Executor requestResolverExecutor) {
        resolutionExecutor = requestResolverExecutor;
        resolvableReceiverExecutor = Executors.newSingleThreadExecutor();
    }

    public final void start(Resolver resolver) throws StartupFailedException {
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

    private void startProcessingRequests(Resolver resolver) {
        while (running) {
            ResolvableQuery resolvableQuery;
            try {
                resolvableQuery = getNextResolvableQuery();
            } catch (ResolutionFailedException e) {
                // TODO add error handling
                continue;
            }
            resolutionExecutor.execute(new Task(resolvableQuery, resolver));
        }
    }

    protected abstract void performStartup() throws StartupFailedException;

    protected abstract void performStop() throws ShutdownFailedException;

    protected abstract ResolvableQuery getNextResolvableQuery() throws ResolutionFailedException;

    private static final class Task implements Runnable {

        private final ResolvableQuery resolvableQuery;
        private final Resolver resolver;

        public Task(ResolvableQuery resolvableQuery, Resolver resolver) {
            this.resolvableQuery = resolvableQuery;
            this.resolver = resolver;
        }

        @Override
        public void run() {
            Resolvable resolvable = resolvableQuery.getResolvable();
            try {
                Object result = resolver.resolve(resolvable);
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
