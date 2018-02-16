package com.kush.utils.signaling;

import static com.kush.utils.id.Identifier.id;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.kush.utils.id.Identifier;

public class SignalSpace {

    private final Map<Identifier, Collection<SignalReceiver>> registeredReceivers;

    public SignalSpace() {
        registeredReceivers = new ConcurrentHashMap<>();
    }

    public <R extends SignalReceiver, S extends Signal<R>> void register(Class<S> signalClass, SignalReceiver receiver) {
        Identifier signalId = id(signalClass);
        Collection<SignalReceiver> receivers = registeredReceivers.get(signalId);
        if (receivers == null) {
            receivers = new HashSet<>();
            registeredReceivers.put(signalId, receivers);
        }
        receivers.add(receiver);
    }

    public <R extends SignalReceiver, S extends Signal<R>> void unregister(Class<S> signalClass, SignalReceiver receiver) {
        Identifier signalId = id(signalClass);
        Collection<SignalReceiver> receivers = registeredReceivers.get(signalId);
        if (receivers != null) {
            receivers.remove(receiver);
        }
    }

    public void emit(Signal<?> signal) {
        Identifier signalId = signal.getId();
        Collection<SignalReceiver> receivers = registeredReceivers.get(signalId);
        if (receivers != null) {
            for (SignalReceiver receiver : receivers) {
                signal.emit(receiver);
            }
        }
    }
}
