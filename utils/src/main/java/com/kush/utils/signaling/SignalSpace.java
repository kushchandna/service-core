package com.kush.utils.signaling;

import static com.kush.utils.id.Identifier.id;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import com.kush.utils.id.Identifier;

public class SignalSpace {

    private final Map<Identifier, Collection<SignalReceiver>> registeredReceivers;
    private final Executor executor;
    private final SignalEmitterFactory signalEmitterFactory;

    public SignalSpace(Executor executor, SignalEmitterFactory signalEmitterFactory) {
        registeredReceivers = new ConcurrentHashMap<>();
        this.executor = executor;
        this.signalEmitterFactory = signalEmitterFactory;
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
        executor.execute(new Runnable() {

            @Override
            public void run() {
                findReceiversAndEmit(signal);
            }
        });
    }

    private void findReceiversAndEmit(Signal<?> signal) {
        Identifier signalId = signal.getId();
        Collection<SignalReceiver> receivers = registeredReceivers.get(signalId);
        SignalEmitter emitter = signalEmitterFactory.create();
        if (receivers != null) {
            emitter.emit(signal, receivers);
        }
    }
}
