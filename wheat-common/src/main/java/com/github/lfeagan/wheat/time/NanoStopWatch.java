package com.github.lfeagan.wheat.time;

import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;

public final class NanoStopWatch {
	
	private static final long NANOS_PER_SECOND = 1000000000L;
	private static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * 60;
	private static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * 60;

	private long start = 0L;
	private long stop = 0L;
	private long elapsed = 0L;
	private final List<Split> splits = new Vector<Split>();

	public NanoStopWatch() {}
	
	public NanoStopWatch(boolean start) {
		if (start) {
			this.start();
		}
	}

	/**
	 * Starts the stop watch by setting start to the current time and stop to
	 * zero.
	 */
	public synchronized void start() {
		start = System.nanoTime();
		stop = 0L;
	}

	/**
	 * If restart is <code>true</code>, starts the stop watch by setting start
	 * to the current time. If restart is <code>false</code>, the start time is
	 * unchanged.
	 * 
	 * @param restart
	 *            if <code>true</code> the start time is set to the current
	 *            time, otherwise it is untouched
	 */
	public synchronized void start(final boolean restart) {
		if (start == 0L || restart) {
			start = System.nanoTime();
		}
		stop = 0L;
	}
	
	public synchronized void stop() {
		stop = System.nanoTime();
		elapsed += (stop - start);
	}

	public Duration getDuration() {
		return Duration.ofNanos(getElapsed());
	}

	public Duration getDuration(boolean useCurrentTime) {
		if (useCurrentTime) {
			return Duration.ofNanos(System.nanoTime() - start);
		} else {
			return Duration.ofNanos(getElapsed());
		}
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
	
	public synchronized void split() {
		final long splitStart;
		if (splits.isEmpty()) {
			splitStart = start;
		} else {
			splitStart = splits.get(splits.size()-1).getStop();
		}
		splits.add(new Split(splitStart));
	}
	
	public synchronized void split(final String description) {
		final long splitStart;
		if (splits.isEmpty()) {
			splitStart = start;
		} else {
			splitStart = splits.get(splits.size()-1).getStop();
		}
		splits.add(new Split(splitStart, description));
	}

	public synchronized void splitFromNanoWatchStartTime(final String description) {
		final long splitStart;
		splitStart = start;
		splits.add(new Split(splitStart, description));
	}

	public synchronized void reset() {
		start = 0L;
		stop = 0L;
		elapsed = 0L;
		splits.clear();
	}

	public synchronized long getStart() {
		return this.start;
	}

	public synchronized long getStop() {
		return this.stop;
	}
	
	public synchronized List<Split> getSplits() {
		return Collections.unmodifiableList(splits);
	}

	public synchronized List<Split> getSplits(final String description) {
		final List<Split> matches = new Vector<Split>();
		for (Split split : splits) {
			if (description.equals(split.getDescription())) {
				matches.add(split);
			}
		}
		return matches;
	}

	public synchronized double getSplitsAverageElapsedTime(final String description) {
		double avg=getSplits(description).stream().mapToDouble(Split::getElapsedSeconds)
				.summaryStatistics().getAverage();
		return avg;
	}

	public synchronized List<Split> getSplits(final Matcher descriptionMatcher) {
		final List<Split> matches = new Vector<Split>();
		for (Split split : splits) {
			if (descriptionMatcher.reset(split.getDescription()).matches()) {
				matches.add(split);
			}
		}
		return matches;
	}
	
	public synchronized void clearSplits() {
		this.splits.clear();
	}

	/**
	 * Returns the elapsed time in nanoseconds. If the watch has not been
	 * stopped, returns <code>0</code>.
	 * 
	 * @return the elapsed time in nanoseconds, or zero if the watch has not
	 *         been stopped
	 */
	public synchronized long getElapsed() {
		return elapsed;
	}
	
	public synchronized double getElapsedSeconds() {
		return ((double) elapsed) / NANOS_PER_SECOND;
	}

	public synchronized void printElapsedTime() {
		if (stop != 0) {
			System.out.println("Elapsed Time: " + getElapsed());
		} else {
			if (start == 0) {
				System.out.println("Timer has not been started");
			} else {
				System.out.println("Timer has not been stopped");
			}
		}
	}

	public synchronized void printElapsedTime(final Writer writer) {
		try {
			if (stop != 0) {
				writer.write("Elapsed Time: " + getElapsed() + "\n");
			} else {
				if (start == 0) {
					writer.write("Timer has not been started\n");
				} else {
					writer.write("Timer has not been stopped\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return toString(getElapsed());
	}
	
	
	public static String toString(final long elapsed) {
		long resid = elapsed;

		long hours = resid / NANOS_PER_HOUR; resid %= NANOS_PER_HOUR;
		long minutes = resid / NANOS_PER_MINUTE; resid %= NANOS_PER_MINUTE;
		long seconds = resid / NANOS_PER_SECOND; resid %= NANOS_PER_SECOND;
		
		long micros = resid / 1000L; resid %= 1000L;

//		String msecStr = (!(wantMsec)) ? "" : new StringBuilder().append(".").append((resid < 100L) ? "0" : (resid < 10L) ? "00" : "").append(resid).toString();

		if (hours == 0L && minutes == 0L) {
			final StringBuilder sb = new StringBuilder();
			if (seconds == 0L) {
				sb.append("0.");	
			} else {
				sb.append(seconds);
				sb.append(".");
			}
			sb.append(String.format("%06d", micros));
			return sb.toString();
		} else {
			String sb = ((hours < 10L) ? "0" : "") +
					hours +
					":" +
					((minutes < 10L) ? "0" : "") +
					minutes +
					":" +
					((seconds < 10L) ? "0" : "") +
					seconds +
					"." +
					String.format("%06d", micros);
			return sb;
		}
		
	}

}
