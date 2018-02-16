package com.kush.utils.signaling;

import com.kush.utils.id.Identifier;

public abstract class Signal<S extends SignalReceiver> {

    @SuppressWarnings("unchecked")
    void emit(SignalReceiver receiver) {
        handleSignal((S) receiver);
    }

    protected abstract void handleSignal(S receiver);

    protected final Identifier getId() {
        return Identifier.id(getClass());
    }
}
