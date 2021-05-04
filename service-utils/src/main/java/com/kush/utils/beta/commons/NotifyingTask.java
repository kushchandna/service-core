package com.kush.utils.beta.commons;

import static java.lang.String.format;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;


public class NotifyingTask implements Runnable {

    private final String name;
    private final long executionTime;
    private final TimeUnit timeUnit;
    private final Exception exception;

    private State state;
    private final Stopwatch watch;
    private PrintStream stream = System.out;

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
        watch = Stopwatch.createStarted();
        setState(State.NOT_STARTED);
    }

    public void setPrintStream(PrintStream stream) {
        this.stream = stream;
        printStateUpdate();
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    private void setState(State state) {
        this.state = state;
        printStateUpdate();
    }

    @Override
    public void run() {
        setState(State.STARTED);
        try {
            executeTask();
            setState(State.COMPLETED);
        } catch (InterruptedException e) {
            setState(State.INTERRUPTED);
        } catch (Exception e) {
            setState(State.FAILED);
        }
        watch.stop();
    }

    private void printStateUpdate() {
        stream.println(format("Time: %d ms, Name: %s, State: %s", watch.elapsed(TimeUnit.MILLISECONDS), name, state));
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
