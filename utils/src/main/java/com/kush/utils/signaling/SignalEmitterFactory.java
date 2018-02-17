package com.kush.utils.signaling;

public interface SignalEmitterFactory {

    SignalEmitter create(Signal<?> signal);
}
