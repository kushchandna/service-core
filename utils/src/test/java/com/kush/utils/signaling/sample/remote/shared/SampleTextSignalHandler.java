package com.kush.utils.signaling.sample.remote.shared;

import com.kush.utils.signaling.SignalHandler;

public interface SampleTextSignalHandler extends SignalHandler {

    void onTextReceived(String text);
}
