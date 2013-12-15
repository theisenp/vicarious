package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import twitter4j.Status;

/**
 * Unit tests for {@link IgnoreRepliesFilter}
 * 
 * @author patrick.theisen
 */
public class IgnoreRepliesFilterTest {

	@Test
	public void testAcceptPublicTweet() {
		IgnoreRepliesFilter filter = new IgnoreRepliesFilter();

		Status tweet = mock(Status.class);
		when(tweet.getInReplyToStatusId()).thenReturn(-1L);
		when(tweet.getInReplyToUserId()).thenReturn(-1L);
		when(tweet.getInReplyToScreenName()).thenReturn(null);

		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testRejectPublicDirectMessage() {
		IgnoreRepliesFilter filter = new IgnoreRepliesFilter();

		Status tweet = mock(Status.class);
		when(tweet.getInReplyToStatusId()).thenReturn(-1L);
		when(tweet.getInReplyToUserId()).thenReturn(1L);
		when(tweet.getInReplyToScreenName()).thenReturn("test");

		assertThat(filter.filter(tweet)).isFalse();
	}

	@Test
	public void testRejectReplyToTweet() {
		IgnoreRepliesFilter filter = new IgnoreRepliesFilter();

		Status tweet = mock(Status.class);
		when(tweet.getInReplyToStatusId()).thenReturn(1L);
		when(tweet.getInReplyToUserId()).thenReturn(1L);
		when(tweet.getInReplyToScreenName()).thenReturn("test");

		assertThat(filter.filter(tweet)).isFalse();
	}
}
