package com.kush.serviceclient.notifications;

import com.kush.utils.signaling.SignalHandlerRegistrar;
import com.kush.utils.signaling.sample.handlers.SampleMessageHandler;
import com.kush.utils.signaling.sample.signals.SampleMessageSignal;

public class SampleClient {

    public static void main(String[] args) {
        SignalHandlerRegistrar registrar = new SignalHandlerRegistrar();
        registrar.register(SampleMessageSignal.class, new SampleMessageHandler() {

            @Override
            public void handleMessage(String text) {
                System.out.println("User A received " + text);
            }
        }, "User A");
        registrar.register(SampleMessageSignal.class, new SampleMessageHandler() {

            @Override
            public void handleMessage(String text) {
                System.out.println("User B received " + text);
            }
        }, "User B");
    }
}
