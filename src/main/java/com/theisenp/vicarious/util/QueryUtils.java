package com.theisenp.vicarious.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import twitter4j.Query;

/**
 * Static utilities for working with {@link Query} objects
 * 
 * @author patrick.theisen
 */
public class QueryUtils {

	// Constants
	private static DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
			.appendYear(4, 4).appendLiteral('-').appendMonthOfYear(2)
			.appendLiteral('-').appendDayOfMonth(2).toFormatter();

	/**
	 * @param dateTime
	 * The time to format
	 * @return The query string format of the given time
	 */
	public static String formatDateTime(DateTime dateTime) {
		return FORMATTER.print(dateTime);
	}
}
