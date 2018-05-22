package com.kush.utils.async.remoting.receivers;

import com.kush.utils.async.Request;
import com.kush.utils.async.remoting.receivers.RequestReceiver.ResponseListener;

public class ResolvableRequest {

    private final Request<?> request;
    private final ResponseListener listener;

    public ResolvableRequest(Request<?> request, ResponseListener listener) {
        this.request = request;
        this.listener = listener;
    }

    public Request<?> getRequest() {
        return request;
    }

    public ResponseListener getListener() {
        return listener;
    }
}
