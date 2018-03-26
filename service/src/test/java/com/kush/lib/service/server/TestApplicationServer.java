package com.kush.lib.service.server;

import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.server.local.LocalApplicationServer;

public class TestApplicationServer extends LocalApplicationServer {

    private ServiceRequestResolver serviceRequestResolver;

    @Override
    protected void preStartup(Context context) {
        super.preStartup(context);
    }

    @Override
    protected void postStartup(Context context, ServiceRequestResolver serviceRequestResolver) {
        this.serviceRequestResolver = serviceRequestResolver;
    }

    public ServiceRequestResolver getServiceRequestResolver() {
        return serviceRequestResolver;
    }
}
