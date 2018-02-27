package com.kush.lib.service.sample.server;

import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.Context;
import com.kush.lib.service.server.ContextBuilder;
import com.kush.lib.service.server.local.LocalApplicationServer;

public class SampleServerMain {

    public static void main(String[] args) throws Exception {
        ApplicationServer server = new LocalApplicationServer();
        server.registerService(SampleHelloService.class);
        Context context = prepareContext();
        server.start(context);
    }

    private static Context prepareContext() {
        SampleHelloTextProvider greetingProvider = new SampleHelloTextProvider();
        return ContextBuilder.create()
            .withInstance(SampleHelloTextProvider.class, greetingProvider)
            .build();
    }
}
