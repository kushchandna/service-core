package com.kush.utils.time;

import static com.kush.utils.time.TimeMonitor.ACCURACY_IN_MILLIS;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TimeMonitorAccuracyTest {

    private static final int ITERATIONS = 100;

    private final long elapsedTime;
    private final long accuracy;

    @Parameters
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[][] {
                { 1, ACCURACY_IN_MILLIS },
                { 2, ACCURACY_IN_MILLIS },
                { 3, ACCURACY_IN_MILLIS },
                { 4, ACCURACY_IN_MILLIS },
                { 5, ACCURACY_IN_MILLIS },
                { 10, ACCURACY_IN_MILLIS },
                { 20, ACCURACY_IN_MILLIS },
        });
    }

    public TimeMonitorAccuracyTest(long elapsedTime, long accuracy) {
        this.elapsedTime = elapsedTime;
        this.accuracy = accuracy;
    }

    @Test
    public void checkAccuracy() throws Exception {
        for (int i = 0; i < ITERATIONS; i++) {
            TimeMonitor monitor = new TimeMonitor();
            monitor.start();
            sleep(elapsedTime);
            long actual = monitor.stop();
            assertElapsedTimeIsWithingAcceptableRange(actual, elapsedTime, accuracy);
        }
    }

    private static void assertElapsedTimeIsWithingAcceptableRange(long actualElapsedTime, long expectedElapsedTime,
            long accuracy) {
        assertThat(getActualElapsedTimeLessThanExpectedMessage(actualElapsedTime, expectedElapsedTime),
                actualElapsedTime, is(greaterThanOrEqualTo(expectedElapsedTime)));
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

    private void sleep(long time) throws InterruptedException {
        MILLISECONDS.sleep(time);
    }
}
