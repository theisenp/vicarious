package com.theisenp.vicarious;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import com.theisenp.vicarious.logger.TweetLogger;
import com.theisenp.vicarious.modifier.TweetModifier;
import com.theisenp.vicarious.provider.TweetProvider;
import com.theisenp.vicarious.publisher.PublishSuccessListener;
import com.theisenp.vicarious.publisher.TweetPublisher;

/**
 * Unit tests for {@link VicariousRunner}
 * 
 * @author patrick.theisen
 */
public class VicariousRunnerTest {

	@Test
	public void testRunWithoutLogger() throws TwitterException {
		// Mock a tweet provider
		Status firstOriginal = mockTweet(0);
		Status secondOriginal = mockTweet(1);
		Status thirdOriginal = mockTweet(2);
		TweetProvider provider = mock(TweetProvider.class);
		when(provider.getTweets()).thenReturn(
				Arrays.asList(thirdOriginal, secondOriginal, firstOriginal));

		// Mock a tweet modifier
		StatusUpdate firstResponse = new StatusUpdate("first");
		StatusUpdate secondResponse = new StatusUpdate("second");
		TweetModifier modifier = mock(TweetModifier.class);
		when(modifier.modify(firstOriginal)).thenReturn(firstResponse);
		when(modifier.modify(secondOriginal)).thenReturn(secondResponse);
		when(modifier.modify(thirdOriginal)).thenReturn(null);

		// Mock a tweet publisher
		TweetPublisher publisher = mock(TweetPublisher.class);

		// Mock a vicarious factory
		VicariousFactory factory = mock(VicariousFactory.class);
		when(factory.getTweetProvider()).thenReturn(provider);
		when(factory.getTweetModifier()).thenReturn(modifier);
		when(factory.getTweetPublisher()).thenReturn(publisher);

		// Run the loop
		VicariousRunner.run(factory);
		InOrder inOrder = Mockito.inOrder(publisher);
		inOrder.verify(publisher, times(1)).publish(firstResponse);
		inOrder.verify(publisher, times(1)).publish(secondResponse);
	}

	@Test
	public void testRunWithLogger() throws TwitterException {
		// Mock a tweet provider
		Status firstOriginal = mockTweet(0);
		Status secondOriginal = mockTweet(1);
		Status thirdOriginal = mockTweet(2);
		TweetProvider provider = mock(TweetProvider.class);
		when(provider.getTweets()).thenReturn(
				Arrays.asList(thirdOriginal, secondOriginal, firstOriginal));

		// Mock a tweet modifier
		StatusUpdate firstResponse = new StatusUpdate("first");
		StatusUpdate secondResponse = new StatusUpdate("second");
		TweetModifier modifier = mock(TweetModifier.class);
		when(modifier.modify(firstOriginal)).thenReturn(firstResponse);
		when(modifier.modify(secondOriginal)).thenReturn(secondResponse);
		when(modifier.modify(thirdOriginal)).thenReturn(null);

		// Mock a tweet publisher
		MockTweetPublisher publisher = new MockTweetPublisher();
		publisher.setSuccessful(firstResponse);

		// Mock a tweet logger
		TweetLogger logger = mock(TweetLogger.class);

		// Mock a vicarious factory
		VicariousFactory factory = mock(VicariousFactory.class);
		when(factory.getTweetProvider()).thenReturn(provider);
		when(factory.getTweetModifier()).thenReturn(modifier);
		when(factory.getTweetPublisher()).thenReturn(publisher);
		when(factory.getTweetLogger()).thenReturn(logger);

		// Run the loop
		VicariousRunner.run(factory);
		verify(logger, times(1)).log(firstOriginal, firstResponse);
		verify(logger, times(0)).log(secondOriginal, secondResponse);
		verify(logger, times(0)).log(thirdOriginal, null);
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

	/**
	 * Mock implementation of {@link MockTweetPublisher} that only succeeds for
	 * explicity registered tweets
	 * 
	 * @author patrick.theisen
	 */
	private static class MockTweetPublisher implements TweetPublisher {

		// Data
		private final Set<StatusUpdate> successfulTweets =
				new HashSet<StatusUpdate>();

		@Override
		public void publish(StatusUpdate tweet) {
		}

		@Override
		public void publish(StatusUpdate tweet, PublishSuccessListener listener)
				throws TwitterException {
			if(successfulTweets.contains(tweet)) {
				listener.onPublishSuccess(tweet);
			}
			else {
				throw mock(TwitterException.class);
			}
		}

		/**
		 * Registers the given tweet as successful. Future calls to publish for
		 * this tweet will succeed.
		 * 
		 * @param tweet
		 */
		public void setSuccessful(StatusUpdate tweet) {
			successfulTweets.add(tweet);
		}
	}
}
