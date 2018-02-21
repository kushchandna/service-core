package com.kush.lib.service.server.api;

import com.kush.lib.service.remoting.api.ServiceRequestResolver;

public interface ServiceRequestResolverFactory {

    ServiceRequestResolver create();
}
