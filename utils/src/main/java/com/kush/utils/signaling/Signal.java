package com.kush.utils.signaling;

import java.io.Serializable;

import com.kush.utils.id.Identifier;

public abstract class Signal<S extends SignalHandler> implements Serializable {

    private static final long serialVersionUID = 1L;

    static final Object DEFAULT_FILTER = null;

    private transient final Object filter;

    public Signal() {
        this(DEFAULT_FILTER);
    }

    public Signal(Object filter) {
        this.filter = filter;
    }

    @SuppressWarnings("unchecked")
    void emit(SignalHandler receiver) {
        handleSignal((S) receiver);
    }

    protected abstract void handleSignal(S handler);

    final Object getFilter() {
        return filter;
    }

    final Identifier getId() {
        return Identifier.id(getClass());
    }
}
