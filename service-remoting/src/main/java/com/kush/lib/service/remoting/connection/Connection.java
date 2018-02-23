package com.kush.lib.service.remoting.connection;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;
import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;

public interface Connection {

    void start() throws StartupFailedException;

    void stop() throws ShutdownFailedException;

    void sendRequest(ServiceRequest<?> request) throws ServiceRequestFailedException;

    Object getResult() throws ServiceRequestFailedException;
}
