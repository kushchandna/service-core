package com.kush.lib.service.server.local;

import com.kush.lib.service.remoting.connect.local.LocalAppConnector;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ServiceInitializationFailedException;

public class LocalApplicationServer extends ApplicationServer {

    @Override
    public void start(Context context) throws ServiceInitializationFailedException {
        super.start(context);
        LocalAppConnector.setLocalResolver(getServiceRequestResolver());
    }
}
