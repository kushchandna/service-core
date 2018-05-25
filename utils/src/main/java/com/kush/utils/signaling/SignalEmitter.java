package com.kush.utils.signaling;

import java.util.Collection;

public class SignalEmitter {

    SignalEmitter() {
    }

    final void emit(Signal<?> signal, Collection<SignalHandler> handlers) {
        executeSignalEmissionToHandlers(signal, handlers);
    }

    protected void executeSignalEmissionToHandlers(Signal<?> signal, Collection<SignalHandler> handlers) {
        emitSignalToHandlers(signal, handlers);
    }

    protected void executeSignalEmissionToHandler(Signal<?> signal, SignalHandler handler) {
        emitSignalToHandler(signal, handler);
    }

    protected final void emitSignalToHandlers(Signal<?> signal, Collection<SignalHandler> handlers) {
        for (SignalHandler handler : handlers) {
            executeSignalEmissionToHandler(signal, handler);
        }
    }

    protected final void emitSignalToHandler(Signal<?> signal, SignalHandler handler) {
        signal.emit(handler);
    }
}
