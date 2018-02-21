package com.kush.lib.service.server;

import com.kush.lib.service.remoting.api.ServiceRequestFailedException;

public interface ServiceInvoker {

    Object invoke(Object... args) throws ServiceRequestFailedException;
}