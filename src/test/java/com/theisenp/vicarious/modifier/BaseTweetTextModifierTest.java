package com.theisenp.vicarious.modifier;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import twitter4j.Status;

/**
 * Unit tests for {@link BaseTweetTextModifier}
 * 
 * @author patrick.theisen
 */
public class BaseTweetTextModifierTest {

	// Constants
	private static final String TEXT = "text";

	@Test
	public void testInvalidModificationNull() {
		TweetModifier modifier = new NullTweetTextModifier();
		Status tweet = mockTweet();

		assertThat(modifier.modify(tweet)).isNull();
	}

	@Test
	public void testInvalidModificationEmpty() {
		TweetModifier modifier = new EmptyTweetTextModifier();
		Status tweet = mockTweet();

		assertThat(modifier.modify(tweet)).isNull();
	}

	@Test
	public void testInvalidModificationTooLong() {
		TweetModifier modifier = new TooLongTweetTextModifier();
		Status tweet = mockTweet();

		assertThat(modifier.modify(tweet)).isNull();
	}

	@Test
	public void testValidModification() {
		TweetModifier modifier = new IdenticalTweetTextModifier();
		Status tweet = mockTweet();

		assertThat(modifier.modify(tweet)).isNotNull();
	}

	/**
	 * @return A mock tweet to use for testing
	 */
	private static Status mockTweet() {
		Status tweet = mock(Status.class);
		when(tweet.getText()).thenReturn(TEXT);
		return tweet;
	}

	/**
	 * Extension of {@link BaseTweetTextModifier} that returns null text
	 * 
	 * @author patrick.theisen
	 */
	private static class NullTweetTextModifier extends BaseTweetTextModifier {

		@Override
		protected String modifyText(String text) {
			return null;
		}
	}

	/**
	 * Extension of {@link BaseTweetTextModifier} that returns empty text
	 * 
	 * @author patrick.theisen
	 */
	private static class EmptyTweetTextModifier extends BaseTweetTextModifier {

		@Override
		protected String modifyText(String text) {
			return "";
		}
	}

	/**
	 * Extension of {@link BaseTweetTextModifier} that returns long text
	 * 
	 * @author patrick.theisen
	 */
	private static class TooLongTweetTextModifier extends BaseTweetTextModifier {

		@Override
		protected String modifyText(String text) {
			StringBuilder builder = new StringBuilder(141);
			for(int i = 0; i < 141; i++) {
				builder.append("a");
			}
			return builder.toString();
		}
	}

	/**
	 * Extension of {@link BaseTweetTextModifier} that returns the input text
	 * unmodified
	 * 
	 * @author patrick.theisen
	 */
	private static class IdenticalTweetTextModifier extends
			BaseTweetTextModifier {

		@Override
		protected String modifyText(String text) {
			return text;
		}
	}
}
