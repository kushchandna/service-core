package com.kush.utils.time;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TimeMonitorTest {

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private TimeMonitor monitor;

    @Before
    public void beforeEachTest() throws Exception {
        monitor = new TimeMonitor();
    }

    @Test
    public void stopWithoutStart_ThrowsException() throws Exception {
        expected.expect(RuntimeException.class);
        monitor.stop();
    }

    @Test
    public void startAlreadyRunningMonitor_ThrowsException() throws Exception {
        monitor.start();
        expected.expect(RuntimeException.class);
        monitor.start();
    }

    @Test
    public void stopImmediatelyAfterStart() throws Exception {
        monitor.start();
        long elapsed = monitor.stop();
        assertThat(elapsed, is(equalTo(0L)));
    }
}
