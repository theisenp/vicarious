package com.theisenp.vicarious;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

import twitter4j.Status;
import twitter4j.StatusUpdate;

import com.theisenp.vicarious.modifier.TweetModifier;
import com.theisenp.vicarious.provider.TweetProvider;
import com.theisenp.vicarious.publisher.TweetPublisher;

/**
 * Unit tests for {@link VicariousRunner}
 * 
 * @author patrick.theisen
 */
public class VicariousRunnerTest {

	@Test
	public void testRun() {
		// Mock a tweet provider
		Status originalTweet = mock(Status.class);
		TweetProvider provider = mock(TweetProvider.class);
		when(provider.getTweets()).thenReturn(Arrays.asList(originalTweet));

		// Mock a tweet modifier
		StatusUpdate modifiedTweet = new StatusUpdate("");
		TweetModifier modifier = mock(TweetModifier.class);
		when(modifier.modify(originalTweet)).thenReturn(modifiedTweet);

		// Mock a tweet publisher
		TweetPublisher publisher = mock(TweetPublisher.class);

		// Mock a vicarious factory
		VicariousFactory factory = mock(VicariousFactory.class);
		when(factory.getTweetProvider()).thenReturn(provider);
		when(factory.getTweetModifier()).thenReturn(modifier);
		when(factory.getTweetPublisher()).thenReturn(publisher);

		// Run the loop
		VicariousRunner.run(factory);
		verify(publisher).publish(modifiedTweet);
	}
}
