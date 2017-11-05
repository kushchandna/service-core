package com.kush.utils.time;

public class TimeMonitor {

    public static final long ACCURACY_IN_MILLIS = 30;

    private long start;
    private boolean running;

    public void start() {
        if (running) {
            throw new IllegalStateException("Monitor already running");
        }
        running = true;
        start = System.currentTimeMillis();
    }

    public long stop() {
        if (!running) {
            throw new IllegalStateException("Monitor not started");
        }
        running = false;
        long end = System.currentTimeMillis();
        return end - start;
    }
}
