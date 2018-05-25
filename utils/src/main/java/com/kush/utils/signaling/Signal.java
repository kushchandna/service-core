package com.kush.utils.signaling;

import com.kush.utils.id.Identifier;

public abstract class Signal<S extends SignalReceiver> {

    static final Object DEFAULT_FILTER = null;

    private final Object filter;

    public Signal() {
        this(DEFAULT_FILTER);
    }

    public Signal(Object filter) {
        this.filter = filter;
    }

    @SuppressWarnings("unchecked")
    void emit(SignalReceiver receiver) {
        handleSignal((S) receiver);
    }

    protected abstract void handleSignal(S receiver);

    final Identifier getId() {
        return Identifier.id(getClass());
    }

    final Object getFilter() {
        return filter;
    }
}
