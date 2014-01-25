package com.theisenp.vicarious.modifier;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Partial implementation of {@link TweetModifier} that transforms the text of
 * tweets
 * 
 * @author patrick.theisen
 */
public abstract class BaseTweetTextModifier implements TweetModifier {

	@Override
	public StatusUpdate modify(Status tweet) {
		String modified = modifyText(tweet.getText());
		return validateText(modified) ? new StatusUpdate(modified) : null;
	}

	/**
	 * @param text
	 * The text to modify
	 * @return An implementation dependent modification of the given text, or
	 * null if the modification fails
	 */
	protected abstract String modifyText(String text);

	/**
	 * @param text
	 * The text to check
	 * @return True if the given text represents a valid tweet
	 */
	private static boolean validateText(String text) {
		return text != null && !text.isEmpty() && text.length() <= 140;
	}
}
