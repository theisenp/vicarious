package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import twitter4j.Status;

/**
 * Unit tests for {@link IgnoreRetweetsFilter}
 * 
 * @author patrick.theisen
 */
public class IgnoreRetweetsFilterTest {

	@Test
	public void testAcceptOriginalTweet() {
		IgnoreRetweetsFilter filter = new IgnoreRetweetsFilter();

		Status tweet = mock(Status.class);
		when(tweet.isRetweet()).thenReturn(false);

		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testRejectRetweet() {
		IgnoreRetweetsFilter filter = new IgnoreRetweetsFilter();

		Status tweet = mock(Status.class);
		when(tweet.isRetweet()).thenReturn(true);

		assertThat(filter.filter(tweet)).isFalse();
	}
}
