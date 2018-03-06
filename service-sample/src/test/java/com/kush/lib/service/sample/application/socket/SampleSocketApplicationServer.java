package com.kush.lib.service.sample.application.socket;

import com.kush.lib.service.sample.application.SampleApplication;

public class SampleSocketApplicationServer {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleSocketApplicationImpl();
        application.setupServer();
    }
}
