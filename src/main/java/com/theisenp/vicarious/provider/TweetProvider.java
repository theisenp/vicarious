package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Interface for an object that provides tweets
 * 
 * @author patrick.theisen
 */
public interface TweetProvider {

	/**
	 * @return A list of tweets
	 * @throws TwitterException
	 */
	public List<Status> getTweets() throws TwitterException;
}
