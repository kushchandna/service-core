package com.kush.utils.signaling.sample.handlers;

import com.kush.utils.signaling.SignalHandler;

public interface SampleMessageHandler extends SignalHandler {

    void handleMessage(String text);
}
