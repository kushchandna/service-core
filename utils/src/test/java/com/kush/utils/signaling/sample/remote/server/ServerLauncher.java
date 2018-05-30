package com.kush.utils.signaling.sample.remote.server;

import java.util.concurrent.Executors;

import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.remote.shared.SampleTextSignal;

class ServerLauncher {

    public static void main(String[] args) {
        SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter();
        SignalSpace signalSpace = new SignalSpace(Executors.newSingleThreadExecutor(), signalEmitter);
        signalSpace.emit(new SampleTextSignal("User A", "First Text for User A"));
        signalSpace.emit(new SampleTextSignal("User B", "First Text for User B"));
        signalSpace.emit(new SampleTextSignal("User A", "Second Text for User A"));
    }
}
