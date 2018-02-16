package com.kush.utils.signaling.sample.handlers;

import com.kush.utils.signaling.SignalReceiver;

public interface SampleComponentStatusHandler extends SignalReceiver {

    void onStarted();

    void onStopped();
}
