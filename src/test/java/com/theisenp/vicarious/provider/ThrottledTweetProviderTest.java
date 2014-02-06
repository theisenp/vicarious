package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Unit tests for {@link ThrottledTweetProvider}
 * 
 * @author patrick.theisen
 */
public class ThrottledTweetProviderTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testInvalidMaxCount() {
		thrown.expect(IllegalArgumentException.class);
		new ThrottledTweetProvider(null, 0);
	}

	@Test
	public void testNoTweets() throws TwitterException {
		TweetProvider delegate = mock(TweetProvider.class);
		TweetProvider provider = new ThrottledTweetProvider(delegate, 3);

		List<Status> tweets = new ArrayList<Status>();
		when(delegate.getTweets()).thenReturn(tweets);

		assertThat(provider.getTweets()).isEmpty();
	}

	@Test
	public void testFewerThanMaxTweets() throws TwitterException {
		TweetProvider delegate = mock(TweetProvider.class);
		TweetProvider provider = new ThrottledTweetProvider(delegate, 3);

		List<Status> tweets = new ArrayList<Status>();
		tweets.add(mock(Status.class));
		when(delegate.getTweets()).thenReturn(tweets);

		assertThat(provider.getTweets()).containsOnly(tweets.toArray());
	}

	@Test
	public void testExactlyMaxTweets() throws TwitterException {
		TweetProvider delegate = mock(TweetProvider.class);
		TweetProvider provider = new ThrottledTweetProvider(delegate, 3);

		List<Status> tweets = new ArrayList<Status>();
		for(int i = 0; i < 3; i++) {
			tweets.add(mock(Status.class));
		}
		when(delegate.getTweets()).thenReturn(tweets);

		assertThat(provider.getTweets()).containsOnly(tweets.toArray());
	}

	@Test
	public void testGreaterThanMaxTweets() throws TwitterException {
		TweetProvider delegate = mock(TweetProvider.class);
		TweetProvider provider = new ThrottledTweetProvider(delegate, 3);

		List<Status> tweets = new ArrayList<Status>();
		List<Status> expected = new ArrayList<Status>();
		for(int i = 0; i < 100; i++) {
			Status tweet = mock(Status.class);
			tweets.add(tweet);
			if(i < 3) {
				expected.add(tweet);
			}
		}
		when(delegate.getTweets()).thenReturn(tweets);

		assertThat(provider.getTweets()).containsOnly(expected.toArray());
	}
}
