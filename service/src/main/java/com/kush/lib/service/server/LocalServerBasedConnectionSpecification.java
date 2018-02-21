package com.kush.lib.service.server;

import com.kush.lib.service.remoting.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceRequestResolver;

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
