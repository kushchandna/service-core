package com.kush.utils.signaling.sample.handlers;

import com.kush.utils.signaling.SignalHandler;

public interface SamplePrintHandler extends SignalHandler {

    void onPrint(Object data);
}
