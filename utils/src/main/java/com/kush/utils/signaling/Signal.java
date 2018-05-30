package com.kush.utils.signaling;

import com.kush.utils.id.Identifier;
import com.kush.utils.remoting.Resolvable;

public abstract class Signal<S extends SignalHandler> implements Resolvable {

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
