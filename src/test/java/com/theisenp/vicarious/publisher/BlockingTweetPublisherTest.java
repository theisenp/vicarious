package com.theisenp.vicarious.publisher;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Unit tests for {@link BlockingTweetPublisher}
 * 
 * @author patrick.theisen
 */
public class BlockingTweetPublisherTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testPublishSuccessWithoutListener() throws TwitterException {
		Twitter twitter = mock(Twitter.class);

		StatusUpdate tweet = new StatusUpdate("");
		TweetPublisher publisher = new BlockingTweetPublisher(twitter);
		publisher.publish(tweet);

		verify(twitter, times(1)).updateStatus(tweet);
	}

	@Test
	public void testPublishFailureWithoutListener() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		when(twitter.updateStatus(any(StatusUpdate.class))).thenThrow(
				mock(TwitterException.class));

		StatusUpdate tweet = new StatusUpdate("");
		TweetPublisher publisher = new BlockingTweetPublisher(twitter);

		thrown.expect(TwitterException.class);
		publisher.publish(tweet);
	}

	@Test
	public void testPublishSuccessWithListener() throws TwitterException {
		Twitter twitter = mock(Twitter.class);

		StatusUpdate tweet = new StatusUpdate("");
		PublishSuccessListener listener = mock(PublishSuccessListener.class);
		TweetPublisher publisher = new BlockingTweetPublisher(twitter);
		publisher.publish(tweet, listener);

		verify(twitter, times(1)).updateStatus(tweet);
		verify(listener, times(1)).onPublishSuccess(tweet);
	}

	@Test
	public void testPublishFailureWithListener() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		when(twitter.updateStatus(any(StatusUpdate.class))).thenThrow(
				mock(TwitterException.class));

		StatusUpdate tweet = new StatusUpdate("");
		PublishSuccessListener listener = mock(PublishSuccessListener.class);
		TweetPublisher publisher = new BlockingTweetPublisher(twitter);

		thrown.expect(TwitterException.class);
		publisher.publish(tweet, listener);
	}
}
