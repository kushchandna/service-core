package com.kush.lib.service.server;

import com.kush.lib.service.remoting.api.ServiceRequest;

public interface ServiceInvokerProvider {

    ServiceInvoker getInvoker(ServiceRequest<?> request);
}
