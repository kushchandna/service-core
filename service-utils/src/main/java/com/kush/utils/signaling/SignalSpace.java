package com.kush.utils.signaling;

import java.util.Collection;

public class SignalSpace extends SignalHandlerRegistrar {

    private final SignalEmitter signalEmitter;

    public SignalSpace(SignalEmitter signalEmitter) {
        this.signalEmitter = signalEmitter;
    }

    public final void emit(Signal<?> signal) {
        Collection<SignalHandler> handlers = getSignalHandlers(signal);
        if (handlers != null) {
            signalEmitter.emit(signal, handlers);
        }
    }
}
