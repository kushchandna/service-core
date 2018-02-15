package com.kush.lib.service.remoting.api;

import com.kush.utils.exceptions.ObjectNotFoundException;

public interface RemoteServiceProvider {

    ServiceApi getService(String serviceName) throws ObjectNotFoundException;
}
