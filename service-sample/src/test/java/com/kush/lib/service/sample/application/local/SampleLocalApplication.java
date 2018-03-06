package com.kush.lib.service.sample.application.local;

import com.kush.lib.service.client.api.ApplicationClient;
import com.kush.lib.service.remoting.connect.ServiceConnectionFactory;
import com.kush.lib.service.remoting.connect.local.LocalServiceConnectionFactory;
import com.kush.lib.service.sample.application.SampleApplication;
import com.kush.lib.service.server.ApplicationServer;
import com.kush.lib.service.server.local.LocalApplicationServer;

public class SampleLocalApplication {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleLocalApplicationImpl();
        application.setupServer();
        ApplicationClient client = application.setupClient();
        application.performClientOperations(client);
    }

    private static class SampleLocalApplicationImpl extends SampleApplication {

        @Override
        protected ApplicationServer createServerInstance() {
            return new LocalApplicationServer();
        }

        @Override
        protected ServiceConnectionFactory createServiceConnectionFactory() {
            return new LocalServiceConnectionFactory();
        }
    }
}
