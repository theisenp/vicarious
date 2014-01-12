package com.theisenp.vicarious.publisher;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

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
	 * @throws TwitterException
	 */
	public void publish(StatusUpdate tweet) throws TwitterException;

	/**
	 * Attempts to publish the given tweet, and notifies the given listener of
	 * the result
	 * 
	 * @param tweet
	 * The tweet to publish
	 * @param listener
	 * The listener to notify of success or failure
	 * @throws TwitterException
	 */
	public void publish(StatusUpdate tweet, PublishSuccessListener listener)
			throws TwitterException;
}
