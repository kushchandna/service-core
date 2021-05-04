package com.kush.utils.signaling;

import java.io.Serializable;

import com.kush.utils.id.Identifier;
import com.kush.utils.remoting.Resolvable;

public abstract class Signal<S extends SignalHandler> implements Resolvable, Serializable {

    private static final long serialVersionUID = 1L;

    public static final Object DEFAULT_FILTER = null;

    private final Object filter;

    public Signal() {
        this(DEFAULT_FILTER);
    }

    public Signal(Object filter) {
        this.filter = filter;
    }

    @SuppressWarnings("unchecked")
    final void emit(SignalHandler handler) {
        if (isRemoteHandler(handler)) {
            RemoteSignalSender remoteHandler = asRemoteHandler(handler);
            remoteHandler.send(this);
        } else {
            handleSignal((S) handler);
        }
    }

    protected abstract void handleSignal(S handler);

    final Object getFilter() {
        return filter;
    }

    final Identifier getId() {
        return getSignalId(getClass());
    }

    public static Identifier getSignalId(Class<?> signalType) {
        return Identifier.id(signalType);
    }

    private RemoteSignalSender asRemoteHandler(SignalHandler handler) {
        return (RemoteSignalSender) handler;
    }

    private boolean isRemoteHandler(SignalHandler handler) {
        return handler instanceof RemoteSignalSender;
    }
}
