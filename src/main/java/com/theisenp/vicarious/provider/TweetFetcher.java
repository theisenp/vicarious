package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Interface for an object that fetches tweets. The number, type, and selection
 * of tweets is determined by the implementation
 * 
 * @author patrick.theisen
 */
public interface TweetFetcher {

	/**
	 * Fetches tweets for the given user based on some internal strategy
	 * 
	 * @param twitter
	 * @param user
	 */
	public List<Status> fetch(Twitter twitter, String user);
}
