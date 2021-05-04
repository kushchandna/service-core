package com.kush.lib.service.sample.application.socket;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.serviceclient.ApplicationClient;

public class SampleSocketApplicationClientLauncher {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleSocketApplication();
        ApplicationClient client = application.setupClient();
        application.performClientOperations(client);
    }
}
