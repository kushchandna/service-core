package com.kush.lib.service.sample.application.local;

import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.serviceclient.ApplicationClient;

public class SampleLocalApplicationLauncher {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleLocalApplication();
        application.setupServer();
        ApplicationClient client = application.setupClient();
        application.performClientOperations(client);
        application.stopServer();
    }
}
