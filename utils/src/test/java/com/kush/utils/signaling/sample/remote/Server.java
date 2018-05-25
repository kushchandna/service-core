package com.kush.utils.signaling.sample.remote;

import java.util.concurrent.Executors;

import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.signals.SampleMessageSignal;

class Server {

    private final SignalSpace signalSpace;

    public Server(SignalSpace signalSpace) {
        this.signalSpace = signalSpace;
    }

    public static void main(String[] args) {
        SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter();
        SignalSpace signalSpace = new SignalSpace(Executors.newSingleThreadExecutor(), signalEmitter);
        Server server = new Server(signalSpace);
        server.sendMessageSignal(new SampleMessageSignal("User A", "First Text for User A"));
        server.sendMessageSignal(new SampleMessageSignal("User B", "First Text for User B"));
        server.sendMessageSignal(new SampleMessageSignal("User A", "Second Text for User A"));
    }

    private void sendMessageSignal(SampleMessageSignal messageSignal) {
        signalSpace.emit(messageSignal);
    }
}
