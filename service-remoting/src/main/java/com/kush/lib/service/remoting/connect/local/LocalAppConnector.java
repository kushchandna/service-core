package com.kush.lib.service.remoting.connect.local;

import java.util.concurrent.atomic.AtomicReference;

import com.kush.lib.service.remoting.ServiceRequestResolver;

public class LocalAppConnector {

    private static final AtomicReference<ServiceRequestResolver> LOCAL_RESOLVER = new AtomicReference<>();

    public static void setLocalResolver(ServiceRequestResolver resolver) {
        LOCAL_RESOLVER.set(resolver);
    }

    public static ServiceRequestResolver getLocalResolver() {
        return LOCAL_RESOLVER.get();
    }
}
