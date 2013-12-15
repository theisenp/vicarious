package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Provides tweets by fetching them from a single user's stream
 * 
 * @author patrick.theisen
 */
public class SimpleTweetProvider implements TweetProvider {

	// Data
	private final Twitter twitter;
	private final String user;
	private final TweetFetcher fetcher;

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 * @param user
	 * The user for whom to fetch tweets
	 * @param fetcher
	 * The fetcher to use for pulling tweets from the given user
	 */
	public SimpleTweetProvider(Twitter twitter, String user,
			TweetFetcher fetcher) {
		this.twitter = twitter;
		this.user = user;
		this.fetcher = fetcher;
	}

	@Override
	public List<Status> getTweets() {
		return fetcher.fetch(twitter, user);
	}
}
