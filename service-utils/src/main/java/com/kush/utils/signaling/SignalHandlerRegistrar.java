package com.kush.utils.signaling;

import static com.kush.commons.id.Identifier.id;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kush.commons.id.Identifier;

public class SignalHandlerRegistrar {

    private final Map<Identifier, Map<Object, Collection<SignalHandler>>> registeredHandlers;

    public SignalHandlerRegistrar() {
        registeredHandlers = new ConcurrentHashMap<>();
    }

    public <R extends SignalHandler, S extends Signal<R>> void register(Class<S> signalClass, R handler) {
        this.register(signalClass, handler, Signal.DEFAULT_FILTER);
    }

    public synchronized <R extends SignalHandler, S extends Signal<R>> void register(Class<S> signalClass, R handler,
            Object filter) {
        registerWithoutCheckingType(signalClass, handler, filter);
    }

    public <R extends SignalHandler, S extends Signal<R>> void unregister(Class<S> signalClass, R handler) {
        this.unregister(signalClass, handler, Signal.DEFAULT_FILTER);
    }

    public synchronized <R extends SignalHandler, S extends Signal<R>> void unregister(Class<S> signalClass, R handler,
            Object filter) {
        unregisterWithoutCheckingType(signalClass, handler, filter);
    }

    protected final void registerWithoutCheckingType(Class<? extends Signal<?>> signalClass, SignalHandler handler,
            Object filter) {
        Identifier signalId = id(signalClass);
        Map<Object, Collection<SignalHandler>> filterVsHandlers = registeredHandlers.get(signalId);
        if (filterVsHandlers == null) {
            filterVsHandlers = new HashMap<>();
            registeredHandlers.put(signalId, filterVsHandlers);
        }
        Collection<SignalHandler> handlers = filterVsHandlers.get(filter);
        if (handlers == null) {
            handlers = new HashSet<>();
            filterVsHandlers.put(filter, handlers);
        }
        handlers.add(handler);
    }

    protected final void unregisterWithoutCheckingType(Class<? extends Signal<?>> signalClass, SignalHandler handler,
            Object filter) {
        Identifier signalId = id(signalClass);
        Map<Object, Collection<SignalHandler>> filterVsHandlers = registeredHandlers.get(signalId);
        if (filterVsHandlers != null) {
            Collection<SignalHandler> handlers = filterVsHandlers.get(filter);
            if (handlers != null) {
                handlers.remove(handler);
                if (handlers.isEmpty()) {
                    filterVsHandlers.remove(filter);
                }
                if (filterVsHandlers.isEmpty()) {
                    registeredHandlers.remove(signalId);
                }
            }
        }
    }

    protected synchronized final Collection<SignalHandler> getSignalHandlers(Signal<?> signal) {
        Object filter = signal.getFilter();
        Identifier signalId = signal.getId();
        Map<Object, Collection<SignalHandler>> filterVsHandlers = registeredHandlers.get(signalId);
        if (filterVsHandlers != null) {
            return filterVsHandlers.get(filter);
        }
        return null;
    }
}
