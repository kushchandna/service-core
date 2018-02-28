package com.kush.lib.service.remoting.receiver;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver.ResponseListener;

public class ResolvableServiceRequest {

    private final ServiceRequest request;
    private final ResponseListener listener;

    public ResolvableServiceRequest(ServiceRequest request, ResponseListener listener) {
        this.request = request;
        this.listener = listener;
    }

    public ServiceRequest getRequest() {
        return request;
    }

    public ResponseListener getListener() {
        return listener;
    }
}
