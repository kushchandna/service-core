package com.kush.utils.remoting.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kush.utils.remoting.ResolutionFailedException;
import com.kush.utils.remoting.Resolvable;

public abstract class ResolutionRequestsReceiver {

    private static final com.kush.logger.Logger LOGGER =
            com.kush.logger.LoggerFactory.INSTANCE.getLogger(ResolutionRequestsReceiver.class);

    private final Map<Class<?>, Resolver<?>> registeredResolvers = new ConcurrentHashMap<>();
    private final Executor requestResolverExecutor;
    private final ExecutorService resolvableReceiverExecutor;

    private volatile boolean running = false;

    public ResolutionRequestsReceiver(Executor requestResolverExecutor) {
        this.requestResolverExecutor = requestResolverExecutor;
        resolvableReceiverExecutor = Executors.newSingleThreadExecutor();
    }

    public final void addResolver(Class<? extends Resolvable> resolvableType, Resolver<? extends Resolvable> resolver) {
        registeredResolvers.put(resolvableType, resolver);
    }

    public final void start() throws StartupFailedException {
        LOGGER.info("Starting requests receiver %s", getClass().getName());
        performStartup();
        running = true;
        LOGGER.info("Started requests receiver %s", getClass().getName());
        resolvableReceiverExecutor.execute(new Runnable() {

            @Override
            public void run() {
                startProcessingRequests();
            }
        });
    }

    public final void stop() throws ShutdownFailedException {
        LOGGER.info("Stopping requests receiver %s", getClass().getName());
        performStop();
        resolvableReceiverExecutor.shutdownNow();
        running = false;
        LOGGER.info("Stopped requests receiver %s", getClass().getName());
    }

    private void startProcessingRequests() {
        while (running) {
            ResolutionRequest resolvableQuery;
            try {
                resolvableQuery = getNextResolvableQuery();
            } catch (ResolutionFailedException e) {
                // TODO add error handling
                continue;
            }
            resolve(resolvableQuery);
        }
    }

    private void resolve(ResolutionRequest resolvableQuery) {
        Class<? extends Resolvable> queryType = resolvableQuery.getResolvable().getClass();
        Resolver<?> resolver = getResolver(queryType);
        if (resolver != null) {
            requestResolverExecutor.execute(new Task<>(resolvableQuery, resolver));
        } else {
            LOGGER.error("No resolver found for type %s", queryType);
            throw new IllegalStateException("Could not resolve request");
        }
    }

    private Resolver<?> getResolver(Class<? extends Resolvable> queryType) {
        Resolver<?> resolver = null;
        for (Class<?> resolvableType : registeredResolvers.keySet()) {
            if (resolvableType.isAssignableFrom(queryType)) {
                resolver = registeredResolvers.get(resolvableType);
                break;
            }
        }
        return resolver;
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Resolving %s", resolvable);
            }
            try {
                @SuppressWarnings("unchecked")
                Object result = resolver.resolve((T) resolvable);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Resolved %s, Got result %s", resolvable, result);
                }
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
