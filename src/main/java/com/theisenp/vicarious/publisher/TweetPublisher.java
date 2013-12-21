package com.theisenp.vicarious.publisher;

import twitter4j.StatusUpdate;

/**
 * Interface for an object that publishes tweets
 * 
 * @author patrick.theisen
 */
public interface TweetPublisher {

	/**
	 * Attempts to publish the given tweet
	 * 
	 * @param tweet
	 * The tweet to publish
	 */
	public void publish(StatusUpdate tweet);

	/**
	 * Attempts to publish the given tweet, and notifies the given listener of
	 * the result
	 * 
	 * @param tweet
	 * The tweet to publish
	 * @param listener
	 * The listener to notify of success or failure
	 */
	public void publish(StatusUpdate tweet, PublishSuccessListener listener);
}
