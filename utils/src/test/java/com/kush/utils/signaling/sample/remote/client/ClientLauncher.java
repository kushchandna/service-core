package com.kush.utils.signaling.sample.remote.client;

import com.kush.utils.signaling.SignalHandlerRegistrar;
import com.kush.utils.signaling.sample.remote.shared.SampleTextSignal;
import com.kush.utils.signaling.sample.remote.shared.SampleTextSignalHandler;

public class ClientLauncher {

    public static void main(String[] args) {
        SignalHandlerRegistrar registrar = new SignalHandlerRegistrar();
        registrar.register(SampleTextSignal.class, new SampleTextSignalHandler() {

            @Override
            public void onTextReceived(String text) {
                System.out.println("User A received " + text);
            }
        }, "User A");
        registrar.register(SampleTextSignal.class, new SampleTextSignalHandler() {

            @Override
            public void onTextReceived(String text) {
                System.out.println("User B received " + text);
            }
        }, "User B");
    }
}
