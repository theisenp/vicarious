package com.theisenp.vicarious.provider.filters;

import org.joda.time.DateTime;

import twitter4j.Status;

import com.theisenp.vicarious.provider.TweetFilter;

/**
 * Implementation of {@link TweetFilter} that rejects all tweets before a
 * particular time
 * 
 * @author patrick.theisen
 */
public class EarliestTimeFilter implements TweetFilter {

	// Data
	private final DateTime earliestTime;

	/**
	 * @param earliestTime
	 * The earliest allowed time, inclusive
	 */
	public EarliestTimeFilter(DateTime earliestTime) {
		this.earliestTime = earliestTime;
	}

	@Override
	public boolean filter(Status tweet) {
		DateTime tweetTime = new DateTime(tweet.getCreatedAt());
		return !tweetTime.isBefore(earliestTime);
	}
}
