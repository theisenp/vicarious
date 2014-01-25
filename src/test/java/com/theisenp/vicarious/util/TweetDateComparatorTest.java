package com.theisenp.vicarious.util;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;

import twitter4j.Status;

/**
 * Unit tests for {@link TweetDateComparator}
 * 
 * @author patrick.theisen
 */
public class TweetDateComparatorTest {

	@Test
	public void testLessThan() {
		TweetDateComparator comparator = new TweetDateComparator();
		Status left = mockTweet(0);
		Status right = mockTweet(1);
		assertThat(comparator.compare(left, right)).isNegative();
	}

	@Test
	public void testGreaterThan() {
		TweetDateComparator comparator = new TweetDateComparator();
		Status left = mockTweet(1);
		Status right = mockTweet(0);
		assertThat(comparator.compare(left, right)).isPositive();
	}

	@Test
	public void testEqualTo() {
		TweetDateComparator comparator = new TweetDateComparator();
		Status left = mockTweet(0);
		Status right = mockTweet(0);
		assertThat(comparator.compare(left, right)).isZero();
	}

	/**
	 * @param millis
	 * The time at which the tweet was created
	 * @return A tweet created at the given time
	 */
	private static Status mockTweet(long millis) {
		Status tweet = mock(Status.class);
		when(tweet.getCreatedAt()).thenReturn(new Date(millis));
		return tweet;
	}
}
