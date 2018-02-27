package com.kush.lib.service.remoting.connect;

import java.io.Closeable;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;

public interface ServiceConnection extends Closeable {

    Object resolve(ServiceRequest request) throws ServiceRequestFailedException;
}
