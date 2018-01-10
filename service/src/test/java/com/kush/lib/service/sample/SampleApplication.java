package com.kush.lib.service.sample;

import com.kush.lib.service.sample.client.SampleApplicationClient;
import com.kush.lib.service.sample.server.SampleGreetingProvider;
import com.kush.lib.service.server.api.ApplicationServer;
import com.kush.lib.service.server.api.Context;
import com.kush.lib.service.server.api.ContextBuilder;

public class SampleApplication {

    public static void main(String[] args) {
        SampleGreetingProvider greetingProvider = new SampleGreetingProvider();
        Context context = ContextBuilder.create()
            .withInstance(SampleGreetingProvider.class, greetingProvider)
            .build();
        ApplicationServer server = new ApplicationServer(context);
        server.start();

        SampleApplicationClient client = new SampleApplicationClient();
        client.init(server.getServiceProvider());
    }
}
