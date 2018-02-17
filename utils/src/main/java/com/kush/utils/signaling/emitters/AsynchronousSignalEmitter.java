package com.kush.utils.signaling.emitters;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;

import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalReceiver;

public class AsynchronousSignalEmitter extends SignalEmitter {

    private final Executor signalSpecificExecutor;
    private final Executor receiverSpecificExecutor;

    public AsynchronousSignalEmitter(Executor signalSpecificExecutor, Executor receiverSpecificExecutor) {
        Objects.requireNonNull(signalSpecificExecutor, "signalSpecificExecutor");
        Objects.requireNonNull(receiverSpecificExecutor, "receiverSpecificExecutor");
        this.signalSpecificExecutor = signalSpecificExecutor;
        this.receiverSpecificExecutor = receiverSpecificExecutor;
    }

    @Override
    protected void executeSignalEmissionToReceivers(Signal<?> signal, Collection<SignalReceiver> receivers) {
        signalSpecificExecutor.execute(new Runnable() {

            @Override
            public void run() {
                emitSignalToReceivers(signal, receivers);
            }
        });
    }

    @Override
    protected void executeSignalEmissionToReceiver(Signal<?> signal, SignalReceiver receiver) {
        receiverSpecificExecutor.execute(new Runnable() {

            @Override
            public void run() {
                emitSignalToReceiver(signal, receiver);
            }
        });
    }
}
