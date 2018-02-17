package com.kush.utils.signaling.sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.kush.utils.signaling.DefaultSignalEmitterFactory;
import com.kush.utils.signaling.SignalEmitterFactory;
import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.handlers.SampleComponentStatusHandler;
import com.kush.utils.signaling.sample.handlers.SamplePrintHandler;
import com.kush.utils.signaling.sample.signals.SampleComponentStatusSignal;
import com.kush.utils.signaling.sample.signals.SamplePrintSignal;

public class SampleSignalingE2E {

    public static void main(String[] args) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        SignalEmitterFactory emitterFactory = new DefaultSignalEmitterFactory();
        SignalSpace signalSpace = new SignalSpace(executor, emitterFactory);

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

        component.start();
        component.print("Some data 1");
        component.stop();
        component.print("Some data 2");
        component.start();
        component.print("Some data 3");

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
    }
}
