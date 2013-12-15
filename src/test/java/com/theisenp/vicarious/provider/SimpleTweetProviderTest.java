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
 * Unit tests for {@link SimpleTweetProvider}
 * 
 * @author patrick.theisen
 */
public class SimpleTweetProviderTest {

	@Test
	public void testProvideTweets() {
		Twitter twitter = mock(Twitter.class);
		String user = "user";
		TweetFetcher fetcher = mock(TweetFetcher.class);
		SimpleTweetProvider provider =
				new SimpleTweetProvider(twitter, user, fetcher);

		List<Status> tweets = new ArrayList<Status>();
		when(fetcher.fetch(twitter, user)).thenReturn(tweets);

		assertThat(provider.getTweets()).isEqualTo(tweets);
	}
}
