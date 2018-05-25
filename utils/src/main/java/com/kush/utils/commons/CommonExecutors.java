package com.kush.utils.commons;

import java.util.concurrent.Executor;

public class CommonExecutors {

    public static Executor newThreadExecutor() {
        return new NewThreadExecutor();
    }

    public static Executor sameThreadExecutor() {
        return new SameThreadExecutor();
    }

    private static class NewThreadExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }

    private static class SameThreadExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }
}
