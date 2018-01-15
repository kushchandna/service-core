package com.kush.lib.service.sample.remoting;

import com.kush.lib.service.client.api.ConnectionSpecification;
import com.kush.lib.service.remoting.RemoteServiceProvider;
import com.kush.lib.service.server.api.ApplicationServer;

public class SampleConnectionSpecification implements ConnectionSpecification {

    public SampleConnectionSpecification(ApplicationServer server) {
    }

    @Override
    public RemoteServiceProvider getRemoteServiceProvider() {
        return null;
    }
}
