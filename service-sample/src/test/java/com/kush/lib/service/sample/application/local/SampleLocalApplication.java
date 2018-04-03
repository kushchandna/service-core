package com.kush.lib.service.sample.application.local;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.sample.application.SampleApplication;

public class SampleLocalApplication {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleLocalApplicationImpl();
        application.setupServer();
        ApplicationClient client = application.setupClient();
        application.performClientOperations(client);
    }
}
