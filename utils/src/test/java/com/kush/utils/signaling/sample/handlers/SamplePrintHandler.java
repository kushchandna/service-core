package com.kush.utils.signaling.sample.handlers;

import com.kush.utils.signaling.SignalReceiver;

public interface SamplePrintHandler extends SignalReceiver {

    void onPrint(Object data);
}
