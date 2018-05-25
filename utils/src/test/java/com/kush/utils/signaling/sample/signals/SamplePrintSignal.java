package com.kush.utils.signaling.sample.signals;

import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.sample.handlers.SamplePrintHandler;

public final class SamplePrintSignal extends Signal<SamplePrintHandler> {

    private static final long serialVersionUID = 1L;

    private final Object data;

    public SamplePrintSignal(Object data) {
        this.data = data;
    }

    @Override
    protected void handleSignal(SamplePrintHandler receiver) {
        receiver.onPrint(data);
    }
}
