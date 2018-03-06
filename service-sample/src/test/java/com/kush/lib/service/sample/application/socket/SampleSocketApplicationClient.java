package com.kush.lib.service.sample.application.socket;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.sample.application.SampleApplication;

public class SampleSocketApplicationClient {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleSocketApplicationImpl();
        ApplicationClient client = application.setupClient();
        application.performClientOperations(client);
    }
}
