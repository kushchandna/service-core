package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.api.ConnectionSpecification;
import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public class LocalServerBasedConnectionSpecification implements ConnectionSpecification {

    private final ServiceRequestResolver requestResolver;

    public LocalServerBasedConnectionSpecification(ApplicationServer server) {
        requestResolver = server.getServiceRequestResolver();
    }

    @Override
    public ServiceRequestResolver getResolver() {
        return requestResolver;
    }
}
