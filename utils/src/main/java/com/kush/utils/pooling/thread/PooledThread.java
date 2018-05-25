package com.kush.utils.pooling.thread;

import java.util.concurrent.BlockingQueue;

class PooledThread extends Thread {

    private final BlockingQueue<Runnable> queuedTasks;

    private volatile boolean running = true;

    public PooledThread(BlockingQueue<Runnable> queuedTasks) {
        this.queuedTasks = queuedTasks;
    }

    @Override
    public void run() {
        while (running) {
            Runnable task;
            try {
                task = queuedTasks.take();
            } catch (InterruptedException e) {
                continue;
            }
            task.run();
        }
    }

    void stopPooledThread() {
        running = false;
        interrupt();
    }
}
