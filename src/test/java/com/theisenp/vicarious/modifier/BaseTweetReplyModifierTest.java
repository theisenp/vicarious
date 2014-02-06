package com.theisenp.vicarious.modifier;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.User;

/**
 * Unit tests for {@link BaseTweetReplyModifier}
 * 
 * @author patrick.theisen
 */
public class BaseTweetReplyModifierTest {

	// Constants
	private static final String USER = "user";
	private static final String TEXT = "text";
	private static final long ID = 0L;

	@Test
	public void testInvalidModificationNull() {
		TweetModifier modifier = new NullTweetTextModifier();
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
		TweetModifier modifier = new EmptyTweetTextModifier();
		Status tweet = mockTweet();

		StatusUpdate update = modifier.modify(tweet);
		assertThat(update).isNotNull();
		assertThat(update.getInReplyToStatusId()).isEqualTo(ID);
		assertThat(update.getStatus()).startsWith("@" + USER);
	}

	/**
	 * @return A mock tweet to use for testing
	 */
	private static Status mockTweet() {
		User user = mock(User.class);
		Status tweet = mock(Status.class);
		when(tweet.getUser()).thenReturn(user);
		when(tweet.getText()).thenReturn(TEXT);
		when(tweet.getId()).thenReturn(ID);
		when(user.getScreenName()).thenReturn(USER);
		return tweet;
	}

	/**
	 * Extension of {@link BaseTweetReplyModifier} that returns null text
	 * 
	 * @author patrick.theisen
	 */
	private static class NullTweetTextModifier extends BaseTweetReplyModifier {

		@Override
		protected String respond(User user, String text) {
			return null;
		}
	}

	/**
	 * Extension of {@link BaseTweetReplyModifier} that returns long text
	 * 
	 * @author patrick.theisen
	 */
	private static class TooLongTweetTextModifier extends
			BaseTweetReplyModifier {

		@Override
		protected String respond(User user, String text) {
			int maxLength = 140 - (user.getScreenName().length() + 2);
			StringBuilder builder = new StringBuilder(maxLength + 1);
			for(int i = 0; i < maxLength + 1; i++) {
				builder.append("a");
			}
			return builder.toString();
		}
	}

	/**
	 * Extension of {@link BaseTweetReplyModifier} that returns empty text
	 * 
	 * @author patrick.theisen
	 */
	private static class EmptyTweetTextModifier extends BaseTweetReplyModifier {

		@Override
		protected String respond(User user, String text) {
			return "";
		}
	}
}
