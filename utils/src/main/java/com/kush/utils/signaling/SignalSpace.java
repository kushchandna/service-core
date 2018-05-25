package com.kush.utils.signaling;

import static com.kush.utils.id.Identifier.id;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import com.kush.utils.id.Identifier;

public class SignalSpace {

    private final Map<Identifier, Map<Object, Collection<SignalReceiver>>> registeredReceivers;
    private final Executor executor;
    private final SignalEmitterFactory signalEmitterFactory;

    public SignalSpace(Executor executor, SignalEmitterFactory signalEmitterFactory) {
        registeredReceivers = new ConcurrentHashMap<>();
        this.executor = executor;
        this.signalEmitterFactory = signalEmitterFactory;
    }

    public <R extends SignalReceiver, S extends Signal<R>> void register(Class<S> signalClass, SignalReceiver receiver) {
        this.register(signalClass, receiver, Signal.DEFAULT_FILTER);
    }

    public <R extends SignalReceiver, S extends Signal<R>> void register(Class<S> signalClass, SignalReceiver receiver,
            Object filter) {
        Identifier signalId = id(signalClass);
        Map<Object, Collection<SignalReceiver>> filterVsReceivers = registeredReceivers.get(signalId);
        if (filterVsReceivers == null) {
            filterVsReceivers = new HashMap<>();
            registeredReceivers.put(signalId, filterVsReceivers);
        }
        Collection<SignalReceiver> receivers = filterVsReceivers.get(filter);
        if (receivers == null) {
            receivers = new HashSet<>();
            filterVsReceivers.put(filter, receivers);
        }
        receivers.add(receiver);
    }

    public <R extends SignalReceiver, S extends Signal<R>> void unregister(Class<S> signalClass, SignalReceiver receiver) {
        this.unregister(signalClass, receiver, Signal.DEFAULT_FILTER);
    }

    public <R extends SignalReceiver, S extends Signal<R>> void unregister(Class<S> signalClass, SignalReceiver receiver,
            Object filter) {
        Identifier signalId = id(signalClass);
        Map<Object, Collection<SignalReceiver>> filterVsReceivers = registeredReceivers.get(signalId);
        if (filterVsReceivers != null) {
            Collection<SignalReceiver> receivers = filterVsReceivers.get(filter);
            if (receivers != null) {
                receivers.remove(receiver);
                if (receivers.isEmpty()) {
                    filterVsReceivers.remove(filter);
                }
                if (filterVsReceivers.isEmpty()) {
                    registeredReceivers.remove(signalId);
                }
            }
        }
    }

    public <S extends Signal<?>> boolean hasReceiverForSignal(Class<S> signalClass) {
        Identifier signalId = id(signalClass);
        Map<Object, Collection<SignalReceiver>> filterVsReceivers = registeredReceivers.get(signalId);
        return filterVsReceivers != null
                && filterVsReceivers.values().stream()
                    .anyMatch(rcvrs -> rcvrs != null && !rcvrs.isEmpty());
    }

    public void emit(Signal<?> signal) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                findReceiversAndEmit(signal);
            }
        });
    }

    private void findReceiversAndEmit(Signal<?> signal) {
        Object filter = signal.getFilter();
        Identifier signalId = signal.getId();
        Map<Object, Collection<SignalReceiver>> filterVsReceivers = registeredReceivers.get(signalId);
        if (filterVsReceivers != null) {
            Collection<SignalReceiver> receivers = filterVsReceivers.get(filter);
            SignalEmitter emitter = signalEmitterFactory.create();
            if (receivers != null) {
                emitter.emit(signal, receivers);
            }
        }
    }
}
