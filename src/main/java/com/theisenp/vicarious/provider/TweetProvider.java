package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;

/**
 * Interface for an object that provides tweets
 * 
 * @author patrick.theisen
 */
public interface TweetProvider {

	/**
	 * @return A list of tweets
	 */
	public List<Status> getTweets();
}
