package it.cnr.rm.iac.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This simple class provides facility for measuring time elapsed during
 * execution of other tasks.
 * 
 * @author giuseppe
 *
 */
public class Timer {
	private static Date startDate = null, endDate = null;
	
	private static long start = 0, end = 0;
	
	private static final DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

	/**
	 * This method allows to start the timer.
	 */
	public static void start() {
		startDate = new Date();
		start = System.nanoTime();
	}

	/**
	 * This method allows to stop the timer.
	 */
	public static void end() {
		endDate = new Date();
		end = System.nanoTime();
	}

	/**
	 * Returns the start time into a formatted time string.
	 * @return The formatted start time string.
	 */
	public static String getStartDate() {
		if (startDate == null)
			return null;
		return dateFormat.format(startDate).toString();
	}

	/**
	 * Returns the end time into a formatted time string.
	 * @return The formatted end time string.
	 */
	public static String getEndDate() {
		if (endDate == null)
			return null;
		return dateFormat.format(endDate).toString();
	}

	/**
	 * Returns the elapsed time in seconds.
	 * @return The elapsed time in seconds.
	 */
	public static double getTimeInSeconds() {
		return (double) (end - start) / 1000000000;
	}

	/**
	 * Sets up both start and end values.
	 */
	public static void reset() {
		startDate = null;
		endDate = null;
		start = 0;
		end = 0;
	}
}