package com.kush.lib.service.remoting.connect.local;

import java.io.IOException;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ServiceRequestResolver;
import com.kush.lib.service.remoting.connect.ServiceConnection;

public class LocalServiceConnection implements ServiceConnection {

    private final ServiceRequestResolver resolver;

    public LocalServiceConnection(ServiceRequestResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Object resolve(ServiceRequest request) throws ServiceRequestFailedException {
        return resolver.resolve(request);
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
