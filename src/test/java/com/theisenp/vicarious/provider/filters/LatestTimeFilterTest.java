package com.theisenp.vicarious.provider.filters;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Test;

import twitter4j.Status;

/**
 * Unit tests for {@link LatestTimeFilter}
 * 
 * @author patrick.theisen
 */
public class LatestTimeFilterTest {

	// Constants
	private static final DateTime EARLY_TIME = new DateTime(0);
	private static final DateTime EXACT_TIME = new DateTime(1);
	private static final DateTime LATE_TIME = new DateTime(2);

	@Test
	public void testEarlierTime() {
		LatestTimeFilter filter = new LatestTimeFilter(EXACT_TIME);

		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(EARLY_TIME.toDate());

		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testExactTime() {
		LatestTimeFilter filter = new LatestTimeFilter(EXACT_TIME);

		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(EXACT_TIME.toDate());

		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testLaterTime() {
		LatestTimeFilter filter = new LatestTimeFilter(EXACT_TIME);

		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(LATE_TIME.toDate());

		assertThat(filter.filter(tweet)).isFalse();
	}
}
