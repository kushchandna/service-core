package com.kush.utils.signaling;

import java.util.Collection;

public interface SignalEmitter {

    void emit(Collection<SignalReceiver> receivers);
}
