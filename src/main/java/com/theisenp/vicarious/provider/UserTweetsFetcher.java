package com.theisenp.vicarious.provider;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.theisenp.vicarious.provider.filters.CompositeTweetFilter;
import com.theisenp.vicarious.provider.filters.EarliestTimeFilter;
import com.theisenp.vicarious.provider.filters.IgnoreRepliesFilter;
import com.theisenp.vicarious.provider.filters.IgnoreRetweetsFilter;
import com.theisenp.vicarious.provider.filters.LatestTimeFilter;

/**
 * Implementation of {@link TweetFetcher} that retrieves all tweets authored by
 * a particular user
 * 
 * @author patrick.theisen
 */
public class UserTweetsFetcher implements TweetFetcher {

	// Constants
	private static final DateTime DEFAULT_EARLIEST = new DateTime(0);
	private static final DateTime DEFAULT_LATEST = new DateTime(Long.MAX_VALUE);
	private static final int MAX_PAGE_SIZE = 100;

	// Data
	private final String user;
	private final DateTime earliestTime;
	private final TweetFilter filter;

	/**
	 * @param user
	 * The user for whom to fetch tweets
	 */
	public UserTweetsFetcher(String user) {
		this(user, DEFAULT_EARLIEST);
	}

	/**
	 * @param user
	 * The user for whom to fetch tweets
	 * @param earliest
	 * The earliest time for which to fetch tweets
	 */
	public UserTweetsFetcher(String user, DateTime earliest) {
		this(user, earliest, DEFAULT_LATEST);
	}

	/**
	 * @param user
	 * The user for whom to fetch tweets
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 * @param latestTime
	 * The latest time for which to fetch tweets, inclusive
	 */
	public UserTweetsFetcher(String user, DateTime earliestTime,
			DateTime latestTime) {
		this.user = user;
		this.earliestTime = earliestTime;

		// Build the composite filter
		List<TweetFilter> filters = new ArrayList<TweetFilter>(4);
		filters.add(new IgnoreRepliesFilter());
		filters.add(new IgnoreRetweetsFilter());
		filters.add(new EarliestTimeFilter(earliestTime));
		filters.add(new LatestTimeFilter(latestTime));
		this.filter = new CompositeTweetFilter(filters);
	}

	@Override
	public List<Status> fetch(Twitter twitter) {
		List<Status> result = new ArrayList<Status>();

		try {
			for(int page = 1;; page++) {
				// Fetch the next page of tweets
				Paging paging = new Paging(page, MAX_PAGE_SIZE);
				List<Status> tweets = twitter.getUserTimeline(user, paging);

				// If there are no tweets, stop looking
				if(tweets.isEmpty()) {
					break;
				}

				// Filter the tweets and add them to the result
				result.addAll(filter(tweets));

				// If the earliest tweet is too early, stop looking
				Status earliestTweet = tweets.get(tweets.size() - 1);
				DateTime tweetTime = new DateTime(earliestTweet.getCreatedAt());
				if(tweetTime.isBefore(earliestTime)) {
					break;
				}
			}
		}
		catch(TwitterException exception) {
			return result;
		}

		return result;
	}

	/**
	 * @param unfilteredTweets
	 * @return The provided list of tweets without any replies or retweets
	 */
	private List<Status> filter(List<Status> unfilteredTweets) {
		List<Status> filteredTweets = new ArrayList<Status>();

		for(Status tweet : unfilteredTweets) {
			if(filter.filter(tweet)) {
				filteredTweets.add(tweet);
			}
		}

		return filteredTweets;
	}
}
