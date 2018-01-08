package com.kush.lib.service.server.api;

import java.util.Collection;

public interface ServerConfiguration {

    Collection<Class<Service>> getServices();
}
