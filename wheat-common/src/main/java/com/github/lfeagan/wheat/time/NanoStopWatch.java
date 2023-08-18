package com.github.lfeagan.wheat.time;

import java.io.IOException;
import java.io.Writer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;

/**
 * An object that measures elapsed time in nanoseconds. This class is superior to directly calling <code>System.nanoTime()</code> in a few ways:
 * <ol>
 *     <li>As documented by nanoTime, the value returned has no absolute meaning, and can only be interpreted as relative to another timestamp returned by nanoTime at a different time. Stopwatch is a more effective abstraction because it exposes only these relative values, not the absolute ones.</li>
 * </ol>
 */
public class NanoStopWatch {
	
	protected static final long NANOS_PER_SECOND = 1000000000L;
	protected static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * 60;
	protected static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * 60;

	protected boolean running = false;

	/**
	 * The time that start was called.
	 */
	protected long start = 0L;

	/**
	 * The time that stop was called.
	 */
	protected long stop = 0L;

	/**
	 * The sum of the delta between all the start and stop times.
	 */
	protected long cumulative = 0L;

	protected NanoStopWatch() {}

	public static NanoStopWatch createStarted() {
		NanoStopWatch nsw = new NanoStopWatch();
		nsw.start();
		return nsw;
	}

	public static NanoStopWatch createUnstarted() {
		return new NanoStopWatch();
	}

	public boolean isRunning() {
		return running;
	}

	/**
	 * Starts the stop watch by setting start to the current time and stop to
	 * zero.
	 */
	public void start() {
		if (isRunning()) {
			throw new IllegalStateException("Already started");
		}
		start = System.nanoTime();
		stop = 0L;
		running = true;
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
	public void start(final boolean restart) {
		if (start == 0L || restart) {
			start = System.nanoTime();
		}
		stop = 0L;
	}
	
	public void stop() {
		if (!isRunning()) {
			throw new IllegalStateException("Already stopped");
		}
		stop = System.nanoTime();
		cumulative += (stop - start);
		running = false;
	}

	public void reset() {
		start = 0L;
		stop = 0L;
		cumulative = 0L;
	}

	/**
	 * Returns the cumulative time in nanoseconds. If the watch has not been through
	 * the start-stop cycle at least once, this will return zero.
	 * This value is set to <code>0</code> when the stop watch is reset.
	 * @return
	 */
	public long getCumulative() {
		return cumulative;
	}

	/**
	 * Returns the elapsed time in nanoseconds. If the watch is running,
	 * returns the difference between the current nano time and the start time.
	 *
	 * @return the elapsed time in nanoseconds, or zero if the watch has not
	 *         been stopped
	 */
	public long getElapsed() {
		if (isRunning()) {
			return System.nanoTime() - start;
		} else {
			return stop - start;
		}
	}

	public Duration getElapsedDuration() {
		return Duration.ofNanos(getElapsed());
	}

	public double getElapsedSeconds() {
		return ((double) getElapsed()) / NANOS_PER_SECOND;
	}

	public void printElapsedTime() {
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

	public void printElapsedTime(final Writer writer) {
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
