package com.kush.utils.signaling.sample.signals;

import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.sample.handlers.SampleComponentStatusHandler;

public final class SampleComponentStatusSignal extends Signal<SampleComponentStatusHandler> {

    private final boolean running;

    public SampleComponentStatusSignal(boolean running) {
        this.running = running;
    }

    @Override
    protected void handleSignal(SampleComponentStatusHandler receiver) {
        if (running) {
            receiver.onStarted();
        } else {
            receiver.onStopped();
        }
    }
}
