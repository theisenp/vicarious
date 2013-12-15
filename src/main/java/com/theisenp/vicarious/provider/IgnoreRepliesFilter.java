package com.theisenp.vicarious.provider;

import twitter4j.Status;

/**
 * Implementation of {@link TweetFilter} that rejects direct replies
 * 
 * @author patrick.theisen
 */
public class IgnoreRepliesFilter implements TweetFilter {

	// Constants

	@Override
	public boolean filter(Status tweet) {
		return tweet.getInReplyToScreenName() == null;
	}
}
