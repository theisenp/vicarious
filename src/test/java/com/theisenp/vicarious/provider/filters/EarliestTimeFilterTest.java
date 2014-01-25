package com.theisenp.vicarious.provider.filters;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Test;

import twitter4j.Status;

/**
 * Unit tests for {@link EarliestTimeFilter}
 * 
 * @author patrick.theisen
 */
public class EarliestTimeFilterTest {

	// Constants
	private static final DateTime EARLY_TIME = new DateTime(0);
	private static final DateTime EXACT_TIME = new DateTime(1);
	private static final DateTime LATE_TIME = new DateTime(2);

	@Test
	public void testEarlierTime() {
		EarliestTimeFilter filter = new EarliestTimeFilter(EXACT_TIME);

		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(EARLY_TIME.toDate());

		assertThat(filter.filter(tweet)).isFalse();
	}

	@Test
	public void testExactTime() {
		EarliestTimeFilter filter = new EarliestTimeFilter(EXACT_TIME);

		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(EXACT_TIME.toDate());

		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testLaterTime() {
		EarliestTimeFilter filter = new EarliestTimeFilter(EXACT_TIME);

		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(LATE_TIME.toDate());

		assertThat(filter.filter(tweet)).isTrue();
	}
}
