package com.kush.lib.service.remoting.connect.local;

import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.connect.ServiceConnection;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;

public class LocalServiceConnectionFactory implements ServiceConnectionFactory {

    @Override
    public ServiceConnection createConnection() {
        ServiceRequestResolver localResolver = LocalAppConnector.getLocalResolver();
        return new LocalServiceConnection(localResolver);
    }
}
