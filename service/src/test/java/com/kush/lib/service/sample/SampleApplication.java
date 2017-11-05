package com.kush.lib.service.sample;

import com.kush.lib.service.sample.client.SampleApplicationClient;
import com.kush.lib.service.sample.server.SampleApplicationServer;

public class SampleApplication {

    public static void main(String[] args) {
        SampleApplicationServer server = new SampleApplicationServer();
        server.init();

        SampleApplicationClient client = new SampleApplicationClient();
        client.init(server);
    }
}
