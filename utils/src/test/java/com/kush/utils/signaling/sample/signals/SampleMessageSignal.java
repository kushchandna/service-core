package com.kush.utils.signaling.sample.signals;

import com.kush.utils.signaling.Signal;
import com.kush.utils.signaling.sample.handlers.SampleMessageHandler;

public class SampleMessageSignal extends Signal<SampleMessageHandler> {

    private final String text;

    public SampleMessageSignal(String user, String text) {
        super(user);
        this.text = text;
    }

    @Override
    protected void handleSignal(SampleMessageHandler receiver) {
        receiver.handleMessage(text);
    }
}
