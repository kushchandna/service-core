package com.kush.utils.commons;

import java.util.concurrent.TimeUnit;

public class NotifyingTask implements Runnable {

    private final String name;
    private final long executionTime;
    private final TimeUnit timeUnit;
    private final Exception exception;

    private State state = State.NOT_STARTED;

    public static NotifyingTask aSleepingTask(String name, long executionTime, TimeUnit timeUnit) {
        return new NotifyingTask(name, executionTime, timeUnit, null);
    }

    public static NotifyingTask aFailingTask(String name, Exception exception) {
        return new NotifyingTask(name, -1, null, exception);
    }

    private NotifyingTask(String name, long executionTime, TimeUnit timeUnit, Exception exception) {
        this.name = name;
        this.executionTime = executionTime;
        this.timeUnit = timeUnit;
        this.exception = exception;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    @Override
    public void run() {
        state = State.STARTED;
        try {
            executeTask();
            state = State.COMPLETED;
        } catch (InterruptedException e) {
            state = State.INTERRUPTED;
        } catch (Exception e) {
            state = State.FAILED;
        }
    }

    private void executeTask() throws Exception {
        if (exception != null) {
            throwException();
        } else {
            sleep();
        }
    }

    private void sleep() throws InterruptedException {
        timeUnit.sleep(executionTime);
    }

    private void throwException() throws Exception {
        throw exception;
    }

    public static enum State {
        NOT_STARTED,
        STARTED,
        INTERRUPTED,
        COMPLETED,
        FAILED
    }
}
