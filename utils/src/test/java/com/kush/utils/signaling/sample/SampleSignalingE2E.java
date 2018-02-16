package com.kush.utils.signaling.sample;

import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.handlers.SampleComponentStatusHandler;
import com.kush.utils.signaling.sample.handlers.SamplePrintHandler;
import com.kush.utils.signaling.sample.signals.SampleComponentStatusSignal;
import com.kush.utils.signaling.sample.signals.SamplePrintSignal;

public class SampleSignalingE2E {

    public static void main(String[] args) {

        SignalSpace signalSpace = new SignalSpace();

        SampleComponent component = new SampleComponent(signalSpace);

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
                System.out.println("Reprinted: " + data);
            }
        });

        component.start();
        component.print("Some data 1");
        component.stop();
        component.print("Some data 2");
        component.start();
        component.print("Some data 3");
    }
}
