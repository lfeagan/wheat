package com.github.lfeagan.wheat.time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * An object that measures elapsed time in nanoseconds. This class is superior to directly calling <code>System.nanoTime()</code> in a few ways:
 * <ol>
 *     <li>It supports tracking split with a description.</li>
 *     <li>As documented by nanoTime, the value returned has no absolute meaning, and can only be interpreted as relative to another timestamp returned by nanoTime at a different time. Stopwatch is a more effective abstraction because it exposes only these relative values, not the absolute ones.</li>
 * </ol>
 */
public class SplitNanoStopWatch extends NanoStopWatch {

    /**
     * The completed splits.
     */
    private final List<Split> splits = new ArrayList<>();

    public void reset() {
        super.reset();
        splits.clear();
    }

    public void split() {
        final long splitStart;
        if (splits.isEmpty()) {
            splitStart = start;
        } else {
            splitStart = splits.get(splits.size()-1).getStop();
        }
        splits.add(new Split(splitStart));
    }

    public void split(final String description) {
        final long splitStart;
        if (splits.isEmpty()) {
            splitStart = start;
        } else {
            splitStart = splits.get(splits.size()-1).getStop();
        }
        splits.add(new Split(splitStart, description));
    }

    public void splitFromNanoWatchStartTime(final String description) {
        final long splitStart;
        splitStart = start;
        splits.add(new Split(splitStart, description));
    }

    public List<Split> getSplits() {
        return Collections.unmodifiableList(splits);
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

    public void clearSplits() {
        this.splits.clear();
    }

    public static class Split {
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

        public long getStart() {
            return this.start;
        }

        public long getStop() {
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
