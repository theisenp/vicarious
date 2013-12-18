package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Unit tests for {@link BaseTweetProvider}
 * 
 * @author patrick.theisen
 */
public class BaseTweetProviderTest {

	@Test
	public void testProvideTweets() {
		Twitter twitter = mock(Twitter.class);
		TweetFetcher fetcher = mock(TweetFetcher.class);
		BaseTweetProvider provider = new BaseTweetProvider(twitter, fetcher);

		List<Status> tweets = new ArrayList<Status>();
		when(fetcher.fetch(twitter)).thenReturn(tweets);

		assertThat(provider.getTweets()).isEqualTo(tweets);
	}
}
