package com.github.lfeagan.wheat.time;

import java.io.IOException;
import java.io.Writer;

public final class StopWatch {

	private long start = 0L;
	private long stop = 0L;

	public StopWatch() {

	}

	public StopWatch(boolean start) {
		if (start) {
			this.start();
		}
	}

	/**
	 * Starts the stop watch by setting start to the current time and stop to
	 * zero.
	 */
	public synchronized void start() {
		start = System.currentTimeMillis();
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
			start = System.currentTimeMillis();
		}
		stop = 0L;
	}

	public synchronized void stop() {
		stop = System.currentTimeMillis();
	}

	public synchronized void reset() {
		start = 0L;
		stop = 0L;
	}

	public synchronized long getStart() {
		return this.start;
	}

	public synchronized long getStop() {
		return this.stop;
	}

	/**
	 * Returns the elapsed time in milliseconds. If the watch has not been
	 * stopped, returns <code>0</code>.
	 * 
	 * @return the elapsed time in milliseconds, or zero if the watch has not
	 *         been stopped
	 */
	public synchronized long getElapsed() {
		if (stop != 0) {
			return stop - start;
		} else {
			return 0;
		}
	}
	
	public synchronized long getCurrentElapsed() {
		if (start == 0) {
			return 0L;
		} else {
			return System.currentTimeMillis() - start;
		}
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
		return toString(false);
	}

	public synchronized String toString(final boolean wantMsec) {
		long elapsed = getElapsed();
		long residual = elapsed;

		long h = residual / 3600000L; residual %= 3600000L;
		long m = residual / 60000L; residual %= 60000L;
		long s = residual / 1000L; residual %= 1000L;

		String msecStr = null;
		if (wantMsec) {
			StringBuilder sb = new StringBuilder(".");
			if (residual >= 100L) {
				// residual: [100,999]
				sb.append(residual);
			} else if ( residual > 10L) {
				// residual: [10,99]
				sb.append("0");
				sb.append(residual);
			} else {
				// residual: [0,9]
				sb.append("00");
				sb.append(residual);
			}
			msecStr = sb.toString();
		} else {
			msecStr = "";
		}

//		String msecStr = (!(wantMsec)) ? "" : new StringBuilder().append(".").append((resid < 100L) ? "0" : (resid < 10L) ? "00" : "").append(resid).toString();

		return ((h < 10L) ? "0" : "") + h + ":" + ((m < 10L) ? "0" : "") + m + ":" + ((s < 10L) ? "0" : "") + s + msecStr;
	}

}
