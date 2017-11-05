package com.kush.lib.service.api.client;

import static com.kush.utils.time.TimeMonitor.ACCURACY_IN_MILLIS;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.kush.lib.service.api.client.Responder;
import com.kush.lib.service.api.client.Response;
import com.kush.lib.service.api.client.Response.ResultListener;
import com.kush.lib.service.api.client.ResponseFailedException;
import com.kush.utils.time.TimeMonitor;

public class ResponderTest {

    private static final String RESULT_SUCCESS = "SUCCESS";

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private Responder responder;
    private TimeMonitor timeMonitor;

    @Before
    public void beforeEachTest() throws Exception {
        Executor executor = Executors.newSingleThreadExecutor();
        responder = new Responder(executor);
        timeMonitor = new TimeMonitor();
    }

    @Test
    public void getResult_WhenResultIsDelayed_WaitsForResult() throws Exception {
        int testSleepTime = 100;
        timeMonitor.start();
        Response<String> response = responder.invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                MILLISECONDS.sleep(testSleepTime);
                return RESULT_SUCCESS;
            }
        });
        String result = response.getResult();
        long elapsed = timeMonitor.stop();
        assertElapsedTimeIsWithingAcceptableRange(elapsed, testSleepTime);
        assertThat(result, is(equalTo(RESULT_SUCCESS)));
    }

    @Test
    public void getResult_WhenResultIsDelayed_ThrowsExceptionIfErrorOccured() throws Exception {
        int testSleepTime = 100;
        ResponderTestException testException = new ResponderTestException();
        Response<String> response = responder.invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                MILLISECONDS.sleep(testSleepTime);
                throw testException;
            }
        });
        expected.expect(ResponseFailedException.class);
        expected.expectCause(is(sameInstance(testException)));
        response.getResult();
    }

    @Test
    public void onResult_WhenDelayedResultIsReceived_GetsCallbackWithResult() throws Exception {
        int testSleepTime = 100;
        Response<String> response = responder.invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                MILLISECONDS.sleep(testSleepTime);
                return RESULT_SUCCESS;
            }
        });
        AtomicReference<String> resultHolder = new AtomicReference<>(null);
        response.setResultListener(new ResultListener<String>() {

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
        Response<String> response = responder.invoke(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return RESULT_SUCCESS;
            }
        });
        MILLISECONDS.sleep(testSleepTime);
        AtomicReference<String> resultHolder = new AtomicReference<>(null);
        response.setResultListener(new ResultListener<String>() {

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
        MILLISECONDS.sleep(testSleepTime + ACCURACY_IN_MILLIS + 1);
    }

    private static void assertElapsedTimeIsWithingAcceptableRange(long actualElapsedTime, long expectedElapsedTime) {
        assertThat(getActualElapsedTimeLessThanExpectedMessage(actualElapsedTime, expectedElapsedTime),
                actualElapsedTime, is(greaterThanOrEqualTo(expectedElapsedTime)));
        long accuracy = ACCURACY_IN_MILLIS;
        assertThat(getActualElapsedTimeGreaterThanExpectedMessage(actualElapsedTime, expectedElapsedTime, accuracy),
                actualElapsedTime, is(lessThanOrEqualTo(expectedElapsedTime + accuracy)));
    }

    private static String getActualElapsedTimeGreaterThanExpectedMessage(long actualElapsedTime, long expectedElapsedTime,
            long accuracy) {
        return format(""
                + "Maximum elapsed time found to be [%d ms] "
                + "which is larger than acceptable accuracy of [%d ms] "
                + "for elapsed times of the order of [%d ms]",
                actualElapsedTime, accuracy, expectedElapsedTime);
    }

    private static String getActualElapsedTimeLessThanExpectedMessage(long actualElapsedTime, long expectedElapsedTime) {
        return format(""
                + "Minimum elapsed time found to be [%d ms] "
                + "which is less than actual elapsed time [%d ms]",
                actualElapsedTime, expectedElapsedTime);
    }

    private static class ResponderTestException extends Exception {
        private static final long serialVersionUID = 1L;
    }
}
