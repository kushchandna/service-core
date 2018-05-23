package com.kush.utils.remoting.server;

import com.kush.utils.remoting.Resolvable;
import com.kush.utils.remoting.server.ResolvableProcessor.ResponseListener;

public class ResolvableQuery {

    private final Resolvable resolvable;
    private final ResponseListener listener;

    public ResolvableQuery(Resolvable resolvable, ResponseListener listener) {
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