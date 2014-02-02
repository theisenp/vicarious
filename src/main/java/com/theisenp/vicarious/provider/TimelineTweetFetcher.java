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
public class TimelineTweetFetcher implements IntervalTweetFetcher {

	// Constants
	private static final int MAX_PAGE_SIZE = 100;

	// Data
	private final String user;

	/**
	 * @param user
	 * The user for whom to fetch tweets
	 */
	public TimelineTweetFetcher(String user) {
		this.user = user;
	}

	@Override
	public List<Status> fetch(Twitter twitter) throws TwitterException {
		return fetch(twitter, DEFAULT_EARLIEST);
	}

	@Override
	public List<Status> fetch(Twitter twitter, DateTime earliestTime)
			throws TwitterException {
		return fetch(twitter, earliestTime, DEFAULT_LATEST);
	}

	@Override
	public List<Status> fetch(Twitter twitter, DateTime earliestTime,
			DateTime latestTime) throws TwitterException {
		// Build the composite filter
		List<TweetFilter> filters = new ArrayList<TweetFilter>(4);
		filters.add(new IgnoreRepliesFilter());
		filters.add(new IgnoreRetweetsFilter());
		filters.add(new EarliestTimeFilter(earliestTime));
		filters.add(new LatestTimeFilter(latestTime));
		TweetFilter filter = new CompositeTweetFilter(filters);

		List<Status> result = new ArrayList<Status>();
		for(int page = 1;; page++) {
			// Fetch the next page of tweets
			Paging paging = new Paging(page, MAX_PAGE_SIZE);
			List<Status> tweets = twitter.getUserTimeline(user, paging);

			// If there are no tweets, stop looking
			if(tweets.isEmpty()) {
				break;
			}

			for(Status tweet : tweets) {
				// If the tweet is too early, stop looking
				if(new DateTime(tweet.getCreatedAt()).isBefore(earliestTime)) {
					return result;
				}

				// Filter the tweet and add it to the result
				if(filter.filter(tweet)) {
					result.add(tweet);
				}
			}

			// If the earliest tweet is too early, stop looking
			Status earliestTweet = tweets.get(tweets.size() - 1);
			DateTime tweetTime = new DateTime(earliestTweet.getCreatedAt());
			if(tweetTime.isBefore(earliestTime)) {
				break;
			}
		}

		return result;
	}
}
