package com.theisenp.vicarious.provider.filters;

import com.theisenp.vicarious.provider.TweetFilter;

import twitter4j.Status;

/**
 * Implementation of {@link TweetFilter} that rejects retweets
 * 
 * @author patrick.theisen
 */
public class IgnoreRetweetsFilter implements TweetFilter {

	@Override
	public boolean filter(Status tweet) {
		return !tweet.isRetweet();
	}
}
