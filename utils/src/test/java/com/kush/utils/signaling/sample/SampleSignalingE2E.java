package com.kush.utils.signaling.sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.kush.utils.signaling.SignalEmitter;
import com.kush.utils.signaling.SignalEmitters;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.handlers.SampleComponentStatusHandler;
import com.kush.utils.signaling.sample.handlers.SampleMessageHandler;
import com.kush.utils.signaling.sample.handlers.SamplePrintHandler;
import com.kush.utils.signaling.sample.signals.SampleComponentStatusSignal;
import com.kush.utils.signaling.sample.signals.SampleMessageSignal;
import com.kush.utils.signaling.sample.signals.SamplePrintSignal;

public class SampleSignalingE2E {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        SignalEmitter signalEmitter = SignalEmitters.newSyncEmitter();
        SignalSpace signalSpace = new SignalSpace(signalEmitter);

        SampleComponent component = new SampleComponent(signalSpace);

        component.start();
        component.print("Some data 0");
        component.stop();

        signalSpace.register(SampleComponentStatusSignal.class, new SampleComponentStatusHandler() {

            @Override
            public void onStarted() {
                System.out.println("Got started");
            }

            @Override
            public void onStopped() {
                System.out.println("Got stopped");
            }
        });

        signalSpace.register(SamplePrintSignal.class, new SamplePrintHandler() {

            @Override
            public void onPrint(Object data) {
                System.out.println("Reprinted by Listener1: " + data);
            }
        });

        signalSpace.register(SamplePrintSignal.class, new SamplePrintHandler() {

            @Override
            public void onPrint(Object data) {
                System.out.println("Reprinted by Listener2: " + data);
            }
        });

        signalSpace.register(SampleMessageSignal.class, new SampleMessageHandler() {

            @Override
            public void handleMessage(String text) {
                System.out.println("UserA received " + text);
            }
        }, "UserA");

        signalSpace.register(SampleMessageSignal.class, new SampleMessageHandler() {

            @Override
            public void handleMessage(String text) {
                System.out.println("UserB received " + text);
            }
        }, "UserB");

        component.start();
        component.print("Some data 1");
        component.stop();
        component.print("Some data 2");
        component.start();
        component.print("Some data 3");
        component.sendMessage("UserA", "Text for UserA");
        component.sendMessage("UserB", "Text for UserB");

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
    }
}
