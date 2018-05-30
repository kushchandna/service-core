package com.kush.utils.signaling.sample.remote.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.kush.utils.remoting.server.ResolutionRequestsReceiver;
import com.kush.utils.remoting.server.Resolver;
import com.kush.utils.remoting.server.StartupFailedException;
import com.kush.utils.remoting.server.socket.SocketBasedResolutionRequestsProcessor;
import com.kush.utils.signaling.RemoteSignalsResolver;
import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.remote.shared.SampleTextSignal;
import com.kush.utils.signaling.sample.remote.shared.SampleTextSignalHandler;

public class ClientLauncher {

    private static final int PORT = 8888;

    public static void main(String[] args) {
        SignalEmitter signalEmitter = SignalEmitters.newAsyncEmitter();
        SignalSpace signalSpace = new SignalSpace(Executors.newSingleThreadExecutor(), signalEmitter);

        signalSpace.register(SampleTextSignal.class, new SampleTextSignalHandler() {

            @Override
            public void onTextReceived(String text) {
                System.out.println("User A received " + text);
            }
        }, "User A");
        signalSpace.register(SampleTextSignal.class, new SampleTextSignalHandler() {

            @Override
            public void onTextReceived(String text) {
                System.out.println("User B received " + text);
            }
        }, "User B");

        Executor executor = Executors.newFixedThreadPool(2);
        ResolutionRequestsReceiver<Signal<?>> signalsReceiver = new SocketBasedResolutionRequestsProcessor<>(executor, PORT);
        Resolver<Signal<?>> resolver = new RemoteSignalsResolver(signalSpace);
        try {
            signalsReceiver.start(resolver);
        } catch (StartupFailedException e) {
            e.printStackTrace();
        }
    }
}
