package com.theisenp.vicarious.logger;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Interface for an object that logs tweets and responses
 * 
 * @author patrick.theisen
 */
public interface TweetLogger {

	/**
	 * Logs the given tweet and response in an implementation specific manner
	 * 
	 * @param original
	 * The original tweet
	 * @param response
	 * The vicarious response
	 */
	public void log(Status original, StatusUpdate response);
}
