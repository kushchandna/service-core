package com.kush.utils.async;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Stopwatch;
import com.kush.utils.async.Response.ResultListener;

public class ResponderTest {

    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final long ACCURACY = 10;

    private final Stopwatch watch = Stopwatch.createUnstarted();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private Responder responder;

    @Before
    public void beforeEachTest() throws Exception {
        Executor executor = Executors.newSingleThreadExecutor();
        responder = new Responder(executor);
        watch.reset();
    }

    @Test
    public void getResult_WhenResultIsDelayed_WaitsForResult() throws Exception {
        int testSleepTime = 100;
        watch.start();
        Response<String> response = responder.respond(new Request<String>() {

            @Override
            public String process() throws RequestFailedException {
                sleep(testSleepTime);
                return RESULT_SUCCESS;
            }
        });
        String result = response.getResult();
        long elapsed = watch.stop().elapsed(MILLISECONDS);
        assertElapsedTimeIsWithingAcceptableRange(elapsed, testSleepTime);
        assertThat(result, is(equalTo(RESULT_SUCCESS)));
    }

    @Test
    public void getResult_WhenResultIsDelayed_ThrowsExceptionIfErrorOccured() throws Exception {
        int testSleepTime = 100;
        RequestFailedException testException = new RequestFailedException();
        Response<String> response = responder.respond(new Request<String>() {

            @Override
            public String process() throws RequestFailedException {
                sleep(testSleepTime);
                throw testException;
            }
        });
        expected.expectCause(is(sameInstance(testException)));
        response.getResult();
    }

    @Test
    public void onResult_WhenDelayedResultIsReceived_GetsCallbackWithResult() throws Exception {
        int testSleepTime = 100;
        Response<String> response = responder.respond(new Request<String>() {

            @Override
            public String process() throws RequestFailedException {
                sleep(testSleepTime);
                return RESULT_SUCCESS;
            }
        });
        AtomicReference<String> resultHolder = new AtomicReference<>(null);
        response.addResultListener(new ResultListener<String>() {

            @Override
            public void onResult(String result) {
                resultHolder.set(result);
            }
        });
        waitUntillTestFinishes(testSleepTime);
        assertThat(resultHolder.get(), is(equalTo(RESULT_SUCCESS)));
    }

    @Test
    public void onResult_WhenResultListenerAttachedAfterResultReceived_StillGetsCallbackWithResult() throws Exception {
        long testSleepTime = 100;
        Response<String> response = responder.respond(new Request<String>() {

            @Override
            public String process() throws RequestFailedException {
                return RESULT_SUCCESS;
            }
        });
        MILLISECONDS.sleep(testSleepTime);
        AtomicReference<String> resultHolder = new AtomicReference<>(null);
        response.addResultListener(new ResultListener<String>() {

            @Override
            public void onResult(String result) {
                resultHolder.set(result);
            }
        });
        waitUntillTestFinishes(0);
        assertThat(resultHolder.get(), is(equalTo(RESULT_SUCCESS)));
    }

    @Test
    public void nullResult() throws Exception {
        // TODO
    }

    @Test
    public void errorListener() throws Exception {
        // TODO
    }

    private void waitUntillTestFinishes(int testSleepTime) throws InterruptedException {
        MILLISECONDS.sleep(testSleepTime + 1);
    }

    private static void assertElapsedTimeIsWithingAcceptableRange(long actualElapsedTime, long expectedElapsedTime) {
        assertThat(getActualElapsedTimeLessThanExpectedMessage(actualElapsedTime, expectedElapsedTime),
                actualElapsedTime, is(greaterThanOrEqualTo(expectedElapsedTime - ACCURACY)));
        assertThat(getActualElapsedTimeGreaterThanExpectedMessage(actualElapsedTime, expectedElapsedTime),
                actualElapsedTime, is(lessThanOrEqualTo(expectedElapsedTime + ACCURACY)));
    }

    private static String getActualElapsedTimeGreaterThanExpectedMessage(long actualElapsedTime, long expectedElapsedTime) {
        return format(""
                + "Maximum elapsed time found to be [%d ms] "
                + "which is larger than the expected elapsed time [%d ms]",
                actualElapsedTime, expectedElapsedTime);
    }

    private static String getActualElapsedTimeLessThanExpectedMessage(long actualElapsedTime, long expectedElapsedTime) {
        return format(""
                + "Minimum elapsed time found to be [%d ms] "
                + "which is less than actual elapsed time [%d ms]",
                actualElapsedTime, expectedElapsedTime);
    }

    private void sleep(int testSleepTime) throws RequestFailedException {
        try {
            MILLISECONDS.sleep(testSleepTime);
        } catch (InterruptedException e) {
            throw new RequestFailedException(e);
        }
    }
}
