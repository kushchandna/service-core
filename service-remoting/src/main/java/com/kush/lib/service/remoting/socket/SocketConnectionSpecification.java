package com.kush.lib.service.remoting.socket;

import com.kush.lib.service.remoting.ConnectionSpecification;
import com.kush.lib.service.remoting.ServiceRequestResolver;

public class SocketConnectionSpecification implements ConnectionSpecification {

    @Override
    public ServiceRequestResolver getResolver() {
        return null;
    }
}
