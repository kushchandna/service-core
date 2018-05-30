package com.kush.utils.signaling.sample.remote.shared;

import com.kush.utils.signaling.Signal;

public class SampleTextSignal extends Signal<SampleTextSignalHandler> {

    private static final long serialVersionUID = 1L;

    private final String text;

    public SampleTextSignal(String userName, String text) {
        super(userName);
        this.text = text;
    }

    @Override
    protected void handleSignal(SampleTextSignalHandler handler) {
        handler.onTextReceived(text);
    }
}
