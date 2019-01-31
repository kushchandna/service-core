package com.kush.utils.signaling.sample.remote;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;
import com.kush.utils.signaling.RemoteSignalSpace;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.client.SignalHandlerRegistrationRequest;
import com.kush.utils.signaling.sample.signals.SampleMessageSignal;

public class SampleRemoteSignalServer {

    private static final int PORT = 8888;

    private RemoteSignalSpace signalSpace;

    public static void main(String[] args) {
        SampleRemoteSignalServer server = new SampleRemoteSignalServer();
        server.startServer();
        server.emitFewSignals();
    }

    void startServer() {
        Executor executor = Executors.newFixedThreadPool(5);
        SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter();
        signalSpace = new RemoteSignalSpace(signalEmitter);
        startReceivingRemoteSignalRegistrationRequests(executor, signalSpace);
    }

    void emitFewSignals() {
        signalSpace.emit(new SampleMessageSignal("Remote User A", "Text for Remote User A"));
        signalSpace.emit(new SampleMessageSignal("Remote User B", "Text for Remote User B"));
    }

    private static void startReceivingRemoteSignalRegistrationRequests(Executor executor, RemoteSignalSpace signalSpace) {
        ResolutionRequestsReceiver clientInfoReceiver = new SocketBasedResolutionRequestsProcessor(executor, PORT);
        clientInfoReceiver.addResolver(SignalHandlerRegistrationRequest.class, signalSpace);
        try {
            clientInfoReceiver.start();
        } catch (StartupFailedException e) {
            e.printStackTrace();
        }
    }
}
