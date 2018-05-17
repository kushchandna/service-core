package com.kush.lib.service.sample.application.socket;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.serviceclient.ApplicationClient;

public class SampleSocketApplicationClient {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleSocketApplicationImpl();
        ApplicationClient client = application.setupClient();
        application.performClientOperations(client);
    }
}
