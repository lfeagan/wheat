package com.github.lfeagan.wheat.time;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;

import static com.github.lfeagan.wheat.time.NanoStopWatch.NANOS_PER_SECOND;

/**
 * An object that measures elapsed time in nanoseconds. This class is superior to directly calling <code>System.nanoTime()</code> in a few ways:
 * <ol>
 *     <li>It supports tracking split with a description.</li>
 *     <li>As documented by nanoTime, the value returned has no absolute meaning, and can only be interpreted as relative to another timestamp returned by nanoTime at a different time. Stopwatch is a more effective abstraction because it exposes only these relative values, not the absolute ones.</li>
 * </ol>
 */
public class SplitNanoStopWatch {

    private final NanoStopWatch stopWatch;

    /**
     * The time splits.
     */
    private final Deque<Split> splits = new ConcurrentLinkedDeque<>();

    protected SplitNanoStopWatch(NanoStopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    public static SplitNanoStopWatch createStarted() {
        return new SplitNanoStopWatch(NanoStopWatch.createStarted());
    }

    public static SplitNanoStopWatch createUnstarted() {
        return new SplitNanoStopWatch(NanoStopWatch.createUnstarted());
    }

    public void start() {
        stopWatch.start();
    }

    public void stop() {
        stopWatch.stop();
    }

    public boolean isRunning() {
        return stopWatch.isRunning();
    }

    /**
     * Resets the underlying NanoStopWatch and clears all splits.
     */
    public void reset() {
        stopWatch.reset();
        splits.clear();
    }

    /**
     * Creates an anonymous split.
     * @throws IllegalStateException if the stop watch has not been started
     */
    public void split() {
        if (!stopWatch.isRunning()) {
            throw new IllegalStateException("Stop watch has not been started");
        }
        final long splitStart;
        if (splits.isEmpty()) {
            splitStart = stopWatch.start;
        } else {
            splitStart = splits.peekLast().getStop();
        }
        splits.add(new Split(splitStart));
    }

    /**
     * Creates a split with a description to provide context.
     * @param description a meaningful description to provide context for the split, need not be unique
     * @throws IllegalStateException if the stop watch has not been started
     */
    public void split(final String description) {
        if (!stopWatch.isRunning()) {
            throw new IllegalStateException("Stop watch has not been started");
        }
        final long splitStart;
        if (splits.isEmpty()) {
            splitStart = stopWatch.start;
        } else {
            splitStart = splits.peekLast().getStop();
        }
        splits.add(new Split(splitStart, description));
    }

    public void splitFromNanoWatchStartTime(final String description) {
        if (!stopWatch.isRunning()) {
            throw new IllegalStateException("Stop watch has not been started");
        }
        splits.add(new Split(stopWatch.start, description));
    }

    public Split[] getSplits() {
        return splits.toArray(new Split[splits.size()]);
    }

    public List<Split> getSplits(final String description) {
        final List<Split> matches = new ArrayList<>();
        for (Split split : splits) {
            if (description.equals(split.getDescription())) {
                matches.add(split);
            }
        }
        return matches;
    }

    public double getSplitsAverageElapsedTime(final String description) {
        double avg=getSplits(description).stream().mapToDouble(Split::getElapsedSeconds)
                .summaryStatistics().getAverage();
        return avg;
    }

    public List<Split> getSplits(final Matcher descriptionMatcher) {
        final List<Split> matches = new ArrayList<>();
        for (Split split : splits) {
            if (descriptionMatcher.reset(split.getDescription()).matches()) {
                matches.add(split);
            }
        }
        return matches;
    }

    public static final class Split {
        final long start;
        final long stop;
        final long elapsed;
        final String description;

        Split(final long start) {
            this(start, "");
        }

        public Split(final long start, final String description) {
            this.start = start;
            this.stop = System.nanoTime();
            this.elapsed = stop - start;
            this.description = description;
        }

        private long getStart() {
            return this.start;
        }

        private long getStop() {
            return this.stop;
        }

        public long getElapsed() {
            return this.elapsed;
        }

        public double getElapsedSeconds() {
            return ((double) this.elapsed) / NANOS_PER_SECOND;
        }

        public String getDescription() {
            return this.description;
        }

        @Override
        public String toString() {
            return NanoStopWatch.toString(elapsed);
        }
    }

}
