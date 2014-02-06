package com.theisenp.vicarious.util;

import static org.fest.assertions.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Unit tests for {@link QueryUtils}
 * 
 * @author patrick.theisen
 */
public class QueryUtilsTest {

	@Test
	public void testFormatDateTimeStartOfDay() {
		DateTime input = new DateTime(2000, 1, 2, 0, 0);
		String expected = "2000-01-02";
		assertThat(QueryUtils.formatDateTime(input)).isEqualTo(expected);
	}
	
	@Test
	public void testFormatDateTimeMiddleOfDay() {
		DateTime input = new DateTime(2000, 1, 2, 12, 0);
		String expected = "2000-01-02";
		assertThat(QueryUtils.formatDateTime(input)).isEqualTo(expected);
	}
	
	@Test
	public void testFormatDateTimeEndOfDay() {
		DateTime input = new DateTime(2000, 1, 2, 23, 59);
		String expected = "2000-01-02";
		assertThat(QueryUtils.formatDateTime(input)).isEqualTo(expected);
	}
}
