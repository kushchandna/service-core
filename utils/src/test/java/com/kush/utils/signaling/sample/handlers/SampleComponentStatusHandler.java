package com.kush.utils.signaling.sample.handlers;

import com.kush.utils.signaling.SignalHandler;

public interface SampleComponentStatusHandler extends SignalHandler {

    void onStarted();

    void onStopped();
}
