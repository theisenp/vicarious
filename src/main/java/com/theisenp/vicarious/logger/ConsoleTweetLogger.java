package com.theisenp.vicarious.logger;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Implementation of {@link TweetLogger} that prints tweets to the console
 * 
 * @author patrick.theisen
 */
public class ConsoleTweetLogger implements TweetLogger {

	// Constants
	private static final String MESSAGE_HEADER = "Handled tweet %d from @%s:";
	private static final String MESSAGE_ORIGINAL = "\t[original]: %s";
	private static final String MESSAGE_RESPONSE = "\t[response]: %s";

	@Override
	public void log(Status original, StatusUpdate response) {
		logHeader(original);
		logOriginal(original);
		logResponse(response);
	}

	/**
	 * Prints a message header
	 * 
	 * @param original
	 * The original tweet
	 */
	private static void logHeader(Status original) {
		long id = original.getId();
		String user = original.getUser().getScreenName();
		String message = String.format(MESSAGE_HEADER, id, user);
		System.out.println(message);
	}

	/**
	 * Prints an original tweet
	 * 
	 * @param original
	 * The original tweet
	 */
	private static void logOriginal(Status original) {
		String text = original.getText();
		String message = String.format(MESSAGE_ORIGINAL, text);
		System.out.println(message);
	}

	/**
	 * Prints a response tweet
	 * 
	 * @param response
	 * The response tweet
	 */
	private static void logResponse(StatusUpdate response) {
		String text = response.getStatus();
		String message = String.format(MESSAGE_RESPONSE, text);
		System.out.println(message);
	}
}
