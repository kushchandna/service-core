package com.kush.lib.service.server;


import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import com.kush.lib.service.remoting.ShutdownFailedException;
import com.kush.lib.service.remoting.StartupFailedException;
import com.kush.lib.service.remoting.receiver.ServiceRequestReceiver;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.BaseService;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.local.LocalApplicationServer;

public class TestApplicationServer1 {

    private final Class<? extends BaseService> serviceClass;
    private final Map<Object, Object> contextItems;

    private ApplicationServer server;

    public TestApplicationServer1(Class<? extends BaseService> serviceClass) {
        this(serviceClass, Collections.emptyMap());
    }

    public TestApplicationServer1(Class<? extends BaseService> serviceClass, Map<Object, Object> contextItems) {
        this.serviceClass = serviceClass;
        this.contextItems = contextItems;
    }

    public void start(ServiceRequestReceiver requestReceiver) {
        server = new LocalApplicationServer();
        registerServices(server);
        Context context = createContext();
        startServer(server, context);
    }

    public void stop() {
        try {
            server.stop();
        } catch (ShutdownFailedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void startServer(ApplicationServer server, Context context) {
        try {
            server.start(context);
        } catch (StartupFailedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private Context createContext() {
        ContextBuilder contextBuilder = ContextBuilder.create();
        for (Entry<Object, Object> contextEntry : contextItems.entrySet()) {
            contextBuilder = contextBuilder.withInstance(contextEntry.getKey(), contextEntry.getValue());
        }
        return contextBuilder.build();
    }

    private void registerServices(ApplicationServer server) {
        server.registerService(serviceClass);
    }
}
