package com.kush.lib.service.remoting.connect;

import com.kush.lib.service.remoting.ServiceRequest;
import com.kush.lib.service.remoting.ServiceRequestFailedException;

public interface ServiceConnection extends AutoCloseable {

    Object resolveRequest(ServiceRequest request) throws ServiceRequestFailedException;
}
