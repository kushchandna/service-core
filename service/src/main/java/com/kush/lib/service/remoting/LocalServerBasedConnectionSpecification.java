package com.kush.lib.service.remoting;

import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.server.api.ApplicationServer;

public class LocalServerBasedConnectionSpecification implements ConnectionSpecification {

    public LocalServerBasedConnectionSpecification(ApplicationServer server) {
    }

    @Override
    public RemoteServiceProvider getRemoteServiceProvider() {
        return null;
    }
}
