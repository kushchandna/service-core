package com.kush.utils.practise;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ThreadLocals {

    @Test
    public void test() throws Exception {
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                SampleCounter.INSTANCE.setName("Thread1");
                SampleCounter.INSTANCE.methodOne();
                SampleCounter.INSTANCE.resetName();
            }
        });
        Thread thread2 = new Thread(new Runnable() {

            @Override
            public void run() {
                SampleCounter.INSTANCE.setName("Thread2");
                SampleCounter.INSTANCE.methodTwo();
                SampleCounter.INSTANCE.resetName();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private static class SampleCounter {

        private static final SampleCounter INSTANCE = new SampleCounter();

        private final ThreadLocal<String> name = new ThreadLocal<>();

        private SampleCounter() {
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public void resetName() {
            name.remove();
        }

        public void methodOne() {
            System.out.println("Starting methodOne " + name.get());
            sleep();
            System.out.println("Completed methodOne " + name.get());
        }

        public void methodTwo() {
            System.out.println("Starting methodTwo " + name.get());
            sleep();
            System.out.println("Completed methodTwo " + name.get());
        }

        private void sleep() {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
