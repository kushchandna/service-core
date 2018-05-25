package com.kush.utils.signaling.sample;

import com.kush.utils.signaling.SignalSpace;
import com.kush.utils.signaling.sample.signals.SampleComponentStatusSignal;
import com.kush.utils.signaling.sample.signals.SampleMessageSignal;
import com.kush.utils.signaling.sample.signals.SamplePrintSignal;

public class SampleComponent {

    private final SignalSpace signalSpace;

    private boolean running = false;

    public SampleComponent(SignalSpace signalSpace) {
        this.signalSpace = signalSpace;
    }

    public void start() {
        running = true;
        System.out.println("Component started");
        signalSpace.emit(new SampleComponentStatusSignal(true));
    }

    public void stop() {
        running = false;
        System.out.println("Componenet stopped");
        signalSpace.emit(new SampleComponentStatusSignal(false));
    }

    public void sendMessage(String user, String text) {
        System.out.println("Message sent to user " + user + " with text " + text);
        signalSpace.emit(new SampleMessageSignal(user, text));
    }

    public void print(Object object) {
        if (running) {
            System.out.println("Printed: " + object);
            signalSpace.emit(new SamplePrintSignal(object));
        }
    }
}
