package com.kush.utils.signaling.sample.handlers;

import com.kush.utils.signaling.SignalReceiver;

public interface SampleMessageHandler extends SignalReceiver {

    void handleMessage(String text);
}
