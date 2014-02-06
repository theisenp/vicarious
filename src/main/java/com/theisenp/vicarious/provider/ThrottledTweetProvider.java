package com.theisenp.vicarious.provider;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * Implementation of {@link TweetProvider} that wraps another instance and
 * limits the number of tweets that it provides at one time
 * 
 * @author patrick.theisen
 */
public class ThrottledTweetProvider implements TweetProvider {

	// Data
	private final TweetProvider delegate;
	private final int maxTweets;

	/**
	 * @param delegate
	 * The delegate provider to use
	 * @param maxTweets
	 * The maximum number of tweets to provide
	 */
	public ThrottledTweetProvider(TweetProvider delegate, int maxTweets) {
		this.delegate = delegate;
		this.maxTweets = maxTweets;

		// Validate the input
		if(maxTweets < 1) {
			String message = "The max count must be positive";
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	public List<Status> getTweets() throws TwitterException {
		List<Status> tweets = delegate.getTweets();
		int cap = Math.min(maxTweets, tweets.size());
		return tweets.subList(0, cap);
	}
}
