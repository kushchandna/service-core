package com.kush.utils.signaling;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultSignalEmittter implements SignalEmitter {

    private final List<SignalReceiver> receivers = new CopyOnWriteArrayList<>();

    @Override
    public void registerReceiver(SignalReceiver receiver) {
        receivers.add(receiver);
    }

    @Override
    public void unregisterReceiver(SignalReceiver receiver) {
        receivers.remove(receiver);
    }

    @Override
    public void emit(Signal signal) {
        for (SignalReceiver receiver : receivers) {
            signal.emit(receiver);
        }
    }
}
