package com.kush.utils.signaling;

import java.util.Collection;

public class SignalEmitter {

    final void emit(Signal<?> signal, Collection<SignalReceiver> receivers) {
        executeSignalEmissionToReceivers(signal, receivers);
    }

    protected void executeSignalEmissionToReceivers(Signal<?> signal, Collection<SignalReceiver> receivers) {
        emitSignalToReceivers(signal, receivers);
    }

    protected void executeSignalEmissionToReceiver(Signal<?> signal, SignalReceiver receiver) {
        emitSignalToReceiver(signal, receiver);
    }

    protected final void emitSignalToReceivers(Signal<?> signal, Collection<SignalReceiver> receivers) {
        for (SignalReceiver receiver : receivers) {
            executeSignalEmissionToReceiver(signal, receiver);
        }
    }

    protected final void emitSignalToReceiver(Signal<?> signal, SignalReceiver receiver) {
        signal.emit(receiver);
    }
}
