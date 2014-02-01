package com.theisenp.vicarious.provider;

import java.util.List;

import org.joda.time.DateTime;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Partial implementation of {@link TweetFetcher} that retrieves tweets authored
 * during a particular time interval
 * 
 * @author patrick.theisen
 */
public abstract class IntervalTweetFetcher implements TweetFetcher {

	// Constants
	public static final DateTime DEFAULT_EARLIEST = new DateTime(0);
	public static final DateTime DEFAULT_LATEST = new DateTime(Long.MAX_VALUE);

	// Data
	private final DateTime earliestTime;
	private final DateTime latestTime;

	/**
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 * @param latestTime
	 * The latest time for which to fetch tweets, inclusive
	 */
	public IntervalTweetFetcher(DateTime earliestTime, DateTime latestTime) {
		this.earliestTime = earliestTime;
		this.latestTime = latestTime;
	}

	@Override
	public List<Status> fetch(Twitter twitter) throws TwitterException {
		return fetchInternal(twitter, earliestTime, latestTime);
	}

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 * @param latestTime
	 * The latest time for which to fetch tweets, inclusive
	 * @return Tweets fetched based on some internal strategy
	 * @throws TwitterException
	 */
	protected abstract List<Status> fetchInternal(Twitter twitter,
			DateTime earliestTime, DateTime latestTime) throws TwitterException;
}
