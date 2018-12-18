package com.kush.lib.service.sample.application.socket;

import com.kush.lib.service.sample.application.SampleApplication;

public class SampleSocketApplicationServerLauncher {

    public static void main(String[] args) throws Exception {
        SampleApplication application = new SampleSocketApplication();
        application.setupServer();
    }
}
