package com.github.lfeagan.wheat.time;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SplitNanoStopWatchTest {

    @Test
    public void basic() {
        SplitNanoStopWatch snsw = SplitNanoStopWatch.createUnstarted();
        try {
            snsw.split();
            Assert.fail("no exception");
        } catch (IllegalStateException e) {
            // do nothing
        }
        Assert.assertEquals(snsw.getSplits().length, 0);
        snsw.start();
        Assert.assertTrue(snsw.isRunning(), "running");
        snsw.reset();
        Assert.assertFalse(snsw.isRunning(), "not running");
        Assert.assertEquals(snsw.getSplits().length, 0);
        snsw.start();
        snsw.split();
        Assert.assertEquals(snsw.getSplits().length, 1);
    }
}
