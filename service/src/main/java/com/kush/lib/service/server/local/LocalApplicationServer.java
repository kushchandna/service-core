package com.kush.lib.service.server.local;

import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.connect.local.LocalAppConnector;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;

public class LocalApplicationServer extends ApplicationServer {

    @Override
    protected void postStartup(Context context, ServiceRequestResolver serviceRequestResolver) {
        LocalAppConnector.setLocalResolver(serviceRequestResolver);
    }
}
