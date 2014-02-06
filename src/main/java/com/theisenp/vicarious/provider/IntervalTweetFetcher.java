package com.theisenp.vicarious.provider;

import java.util.List;

import org.joda.time.DateTime;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Extension of {@link TweetFetcher} that adds the ability to fetch tweets
 * authored during a particular time interval
 * 
 * @author patrick.theisen
 */
public interface IntervalTweetFetcher extends TweetFetcher {

	// Constants
	public static final DateTime DEFAULT_EARLIEST = new DateTime(0);
	public static final DateTime DEFAULT_LATEST = new DateTime(Long.MAX_VALUE);

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 * @return Tweets in the given interval, fetched based on some internal
	 * strategy
	 * @throws TwitterException
	 */
	public List<Status> fetch(Twitter twitter, DateTime earliestTime)
			throws TwitterException;

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 * @param latestTime
	 * The latest time for which to fetch tweets, inclusive
	 * @return Tweets in the given interval, fetched based on some internal
	 * strategy
	 * @throws TwitterException
	 */
	public List<Status> fetch(Twitter twitter, DateTime earliestTime,
			DateTime latestTime) throws TwitterException;
}
