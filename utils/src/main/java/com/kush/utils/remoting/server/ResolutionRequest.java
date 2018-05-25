package com.kush.utils.remoting.server;

import com.kush.utils.remoting.Resolvable;
import com.kush.utils.remoting.server.ResolutionRequestsReceiver.ResponseListener;

public class ResolutionRequest {

    private final Resolvable resolvable;
    private final ResponseListener listener;

    public ResolutionRequest(Resolvable resolvable, ResponseListener listener) {
        this.resolvable = resolvable;
        this.listener = listener;
    }

    public Resolvable getResolvable() {
        return resolvable;
    }

    public ResponseListener getListener() {
        return listener;
    }
}