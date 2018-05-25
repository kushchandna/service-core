package com.kush.utils.signaling;

import java.util.Collection;
import java.util.concurrent.Executor;

public class SignalSpace extends SignalHandlerRegistrar {

    private final Executor executor;
    private final SignalEmitter signalEmitter;

    public SignalSpace(Executor executor, SignalEmitter signalEmitter) {
        this.signalEmitter = signalEmitter;
        this.executor = executor;
    }

    public void emit(Signal<?> signal) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                findHandlersAndEmit(signal);
            }
        });
    }

    private void findHandlersAndEmit(Signal<?> signal) {
        Collection<SignalHandler> handlers = getSignalHandlers(signal);
        if (handlers != null) {
            signalEmitter.emit(signal, handlers);
        }
    }
}
