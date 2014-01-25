package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Provides tweets by fetching them from Twitter
 * 
 * @author patrick.theisen
 */
public class BaseTweetProvider implements TweetProvider {

	// Data
	private final Twitter twitter;
	private final TweetFetcher fetcher;

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 * @param fetcher
	 * The fetcher to use for pulling tweets from Twitter
	 */
	public BaseTweetProvider(Twitter twitter, TweetFetcher fetcher) {
		this.twitter = twitter;
		this.fetcher = fetcher;
	}

	@Override
	public List<Status> getTweets() throws TwitterException {
		return fetcher.fetch(twitter);
	}
}
