package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Interface for an object that fetches tweets from Twitter. The number, type,
 * and selection of tweets is determined by the implementation
 * 
 * @author patrick.theisen
 */
public interface TweetFetcher {

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 * @return Tweets fetched based on som einternal strategy
	 * @throws TwitterException
	 */
	public List<Status> fetch(Twitter twitter) throws TwitterException;
}
