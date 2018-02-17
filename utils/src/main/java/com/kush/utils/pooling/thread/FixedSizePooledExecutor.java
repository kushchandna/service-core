package com.kush.utils.pooling.thread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

public class FixedSizePooledExecutor implements Executor, AutoCloseable {

    private final List<PooledThread> pooledThreads = new ArrayList<>();
    private final BlockingQueue<Runnable> queuedTasks;

    public FixedSizePooledExecutor(int size, int maxQueuedTasksCount) {
        queuedTasks = new ArrayBlockingQueue<>(maxQueuedTasksCount);
        initializeThreads(size);
    }

    @Override
    public void execute(Runnable command) {
        try {
            queuedTasks.put(command);
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("Queue of tasks is already full");
        }
    }

    @Override
    public void close() throws Exception {
        for (Iterator<PooledThread> iterator = pooledThreads.iterator(); iterator.hasNext();) {
            PooledThread thread = iterator.next();
            thread.stopPooledThread();
            iterator.remove();
        }
    }

    private void initializeThreads(int size) {
        PooledThread thread = new PooledThread(queuedTasks);
        pooledThreads.add(thread);
        thread.start();
    }
}
