package com.kush.lib.service.remoting.local;

import com.kush.lib.service.remoting.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceRequestResolver;

public class LocalServerBasedConnectionSpecification implements ConnectionSpecification {

    @Override
    public ServiceRequestResolver getResolver() {
        return LocalAppConnector.getLocalResolver();
    }
}
