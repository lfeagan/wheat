package com.github.lfeagan.wheat.time;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NanoStopWatchTest {

    @Test
    public void unstarted() {
        NanoStopWatch sw = NanoStopWatch.createUnstarted();
        Assert.assertFalse(sw.isRunning());
        Assert.assertEquals(sw.elapsedNanos(), 0L);
        Assert.assertEquals(sw.elapsedSeconds(), 0.0);
        Assert.assertEquals(sw.getCumulative(), 0L);
        try {
            sw.stop();
            Assert.fail("no exception thrown");
        } catch (IllegalStateException e) {
            // do nothing
        }
        sw.start();
        Assert.assertTrue(sw.isRunning());
        Assert.assertTrue(sw.elapsedNanos() > 0L);
        Assert.assertTrue(sw.elapsedSeconds() > 0.0);
        Assert.assertEquals(sw.getCumulative(), 0L);

        sw.stop();
        Assert.assertFalse(sw.isRunning());
        Assert.assertTrue(sw.elapsedNanos() > 0L);
        Assert.assertTrue(sw.elapsedSeconds() > 0.0);
        Assert.assertTrue(sw.getCumulative() > 0L);
    }

    @Test
    public void started() {
        NanoStopWatch sw = NanoStopWatch.createStarted();
        Assert.assertTrue(sw.isRunning());
        Assert.assertTrue(sw.elapsedNanos() > 0L);
        Assert.assertTrue(sw.elapsedSeconds() > 0.0);
        Assert.assertEquals(sw.getCumulative(), 0L);
    }
}
