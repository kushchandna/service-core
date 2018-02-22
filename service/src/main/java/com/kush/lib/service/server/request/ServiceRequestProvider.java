package com.kush.lib.service.server.request;

import com.kush.lib.service.remoting.ServiceRequest;

public class ServiceRequestProvider {

    private final ServiceRequest<?> request;

    public ServiceRequestProvider(ServiceRequest<?> request) {
        this.request = request;
    }

    public ServiceRequest<?> getServiceRequest() {
        return request;
    }
}
