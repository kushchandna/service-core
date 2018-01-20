package com.kush.lib.service.remoting.api;

import com.kush.lib.service.server.api.NoSuchServiceExistsException;

public interface RemoteServiceProvider {

    ServiceApi getService(String serviceName) throws NoSuchServiceExistsException;
}
