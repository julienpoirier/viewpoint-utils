package fr.viewpoint.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Used to separate JDK System class and easily mock date
 * 
 * @author jpoirier
 * 
 */
public class SystemTime {

	private static TimeSource source = null;

	/**
	 * Build the default time source
	 */
	private static final TimeSource defaultSrc = new TimeSource() {
		public long millis() {
			return System.currentTimeMillis();
		}
	};

	/**
	 * Return the time as millisecond
	 * 
	 * @return
	 */
	public static long asMillis() {
		return getTimeSource().millis();
	}

	/**
	 * Return a new Date
	 * 
	 * @return
	 */
	public static Date asDate() {
		return new Date(asMillis());
	}

	/**
	 * Return a new Calendar
	 * 
	 * @return
	 */
	public static Calendar asCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(asMillis());
		return cal;
	}

	/**
	 * For test purpose
	 */
	public static void reset() {
		setTimeSource(null);
	}

	/**
	 * For test purpose
	 */
	public static void setTimeSource(TimeSource source) {
		SystemTime.source = source;
	}

	/**
	 * For test purpose
	 */
	private static TimeSource getTimeSource() {
		return (source != null ? source : defaultSrc);
	}
}
