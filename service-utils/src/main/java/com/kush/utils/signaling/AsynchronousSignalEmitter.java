package com.kush.utils.signaling;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;

class AsynchronousSignalEmitter extends SignalEmitter {

    private final Executor signalSpecificExecutor;
    private final Executor receiverSpecificExecutor;

    AsynchronousSignalEmitter(Executor signalSpecificExecutor, Executor receiverSpecificExecutor) {
        Objects.requireNonNull(signalSpecificExecutor, "signalSpecificExecutor");
        Objects.requireNonNull(receiverSpecificExecutor, "receiverSpecificExecutor");
        this.signalSpecificExecutor = signalSpecificExecutor;
        this.receiverSpecificExecutor = receiverSpecificExecutor;
    }

    @Override
    protected void executeSignalEmissionToHandlers(Signal<?> signal, Collection<SignalHandler> receivers) {
        signalSpecificExecutor.execute(new Runnable() {

            @Override
            public void run() {
                emitSignalToHandlers(signal, receivers);
            }
        });
    }

    @Override
    protected void executeSignalEmissionToHandler(Signal<?> signal, SignalHandler receiver) {
        receiverSpecificExecutor.execute(new Runnable() {

            @Override
            public void run() {
                emitSignalToHandler(signal, receiver);
            }
        });
    }
}
