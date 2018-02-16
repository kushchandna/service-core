package com.kush.utils.signaling;

public interface SignalEmitter {

    void registerReceiver(SignalReceiver listener);

    void unregisterReceiver(SignalReceiver listener);

    void emit(Signal signal);
}
