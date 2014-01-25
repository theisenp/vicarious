package com.theisenp.vicarious.provider.filters;

import org.joda.time.DateTime;

import twitter4j.Status;

import com.theisenp.vicarious.provider.TweetFilter;

/**
 * Implementation of {@link TweetFilter} that rejects all tweets after a
 * particular time
 * 
 * @author patrick.theisen
 */
public class LatestTimeFilter implements TweetFilter {

	// Data
	private final DateTime latestTime;

	/**
	 * @param latestTime
	 * The latest allowed time, inclusive
	 */
	public LatestTimeFilter(DateTime latestTime) {
		this.latestTime = latestTime;
	}

	@Override
	public boolean filter(Status tweet) {
		DateTime tweetTime = new DateTime(tweet.getCreatedAt());
		return !tweetTime.isAfter(latestTime);
	}
}
