package com.theisenp.vicarious.modifier;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Interface for an object that modifies tweets.
 * 
 * @author patrick.theisen
 */
public interface TweetModifier {

	/**
	 * @param tweet
	 * The tweet to modify
	 * @return An implementation dependent modification of the given tweet, or
	 * null if the modification fails
	 */
	public StatusUpdate modify(Status tweet);
}
