package com.theisenp.vicarious;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import twitter4j.Status;
import twitter4j.StatusUpdate;

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
	public void testRunWithoutLogger() {
		// Mock a tweet provider
		Status firstOriginal = mock(Status.class);
		Status secondOriginal = mock(Status.class);
		Status thirdOriginal = mock(Status.class);
		TweetProvider provider = mock(TweetProvider.class);
		when(provider.getTweets()).thenReturn(
				Arrays.asList(firstOriginal, secondOriginal, thirdOriginal));

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
		verify(publisher, times(1)).publish(firstResponse);
		verify(publisher, times(1)).publish(secondResponse);
	}

	@Test
	public void testRunWithLogger() {
		// Mock a tweet provider
		Status firstOriginal = mock(Status.class);
		Status secondOriginal = mock(Status.class);
		Status thirdOriginal = mock(Status.class);
		TweetProvider provider = mock(TweetProvider.class);
		when(provider.getTweets()).thenReturn(
				Arrays.asList(firstOriginal, secondOriginal, thirdOriginal));

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
		public void publish(StatusUpdate tweet, PublishSuccessListener listener) {
			if(successfulTweets.contains(tweet)) {
				listener.onPublishSuccess(tweet);
			}
			else {
				listener.onPublishFailure(tweet);
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
