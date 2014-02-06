package com.theisenp.vicarious.modifier;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.User;

/**
 * Implementation of {@link TweetModifier} that responds to tweets
 * 
 * @author patrick.theisen
 */
public abstract class BaseTweetReplyModifier implements TweetModifier {

	@Override
	public StatusUpdate modify(Status tweet) {
		User user = tweet.getUser();
		String name = user.getScreenName();
		String response = respond(user, tweet.getText());

		// Check to see if the response is valid
		if(!validateText(name, response)) {
			return null;
		}

		// Build the response
		StringBuilder builder = new StringBuilder();
		builder.append("@").append(name).append(" ").append(response);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(tweet.getId());
		return update;
	}

	/**
	 * @param user
	 * The user to respond to
	 * @param text
	 * The text to respond to
	 * @return An implementation dependent response to the given text from the
	 * given user, or null if the response fails
	 */
	protected abstract String respond(User user, String text);

	/**
	 * @param user
	 * The user to respond to
	 * @param text
	 * The text to check
	 * @return True if the given text represents a valid tweet
	 */
	private static boolean validateText(String user, String text) {
		int available = 140 - (user.length() + 2);
		return text != null && text.length() <= available;
	}
}
