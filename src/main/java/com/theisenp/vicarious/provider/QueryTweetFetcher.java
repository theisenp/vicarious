package com.theisenp.vicarious.provider;

import static com.theisenp.vicarious.util.QueryUtils.formatDateTime;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.theisenp.vicarious.provider.filters.CompositeTweetFilter;
import com.theisenp.vicarious.provider.filters.EarliestTimeFilter;
import com.theisenp.vicarious.provider.filters.LatestTimeFilter;

/**
 * Extension of {@link IntervalTweetFetcher} that retrieves tweets based on a
 * {@link Query}
 * 
 * @author patrick.theisen
 */
public class QueryTweetFetcher extends IntervalTweetFetcher {

	// Constants
	private static final int MAX_COUNT = 100;

	// Data
	private final String query;
	private final TweetFilter intervalFilter;

	/**
	 * @param query
	 * The query string
	 */
	public QueryTweetFetcher(String query) {
		this(query, DEFAULT_EARLIEST);
	}

	/**
	 * @param query
	 * The query string
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 */
	public QueryTweetFetcher(String query, DateTime earliestTime) {
		this(query, earliestTime, DEFAULT_LATEST);
	}

	/**
	 * @param query
	 * The query string
	 * @param earliestTime
	 * The earliest time for which to fetch tweets, inclusive
	 * @param latestTime
	 * The latest time for which to fetch tweets, inclusive
	 */
	public QueryTweetFetcher(String query, DateTime earliestTime,
			DateTime latestTime) {
		super(earliestTime, latestTime);
		this.query = query;

		// Build the interval filter
		TweetFilter earlyFilter = new EarliestTimeFilter(earliestTime);
		TweetFilter lateFilter = new LatestTimeFilter(latestTime);
		this.intervalFilter = new CompositeTweetFilter(earlyFilter, lateFilter);
	}

	@Override
	protected List<Status> fetchInternal(Twitter twitter,
			DateTime earliestTime, DateTime latestTime) throws TwitterException {
		// Compute the query's upper bound
		DateTime now = DateTime.now();
		boolean isFuture = latestTime.isAfter(now);
		DateTime upperBound = (isFuture ? now : latestTime).plusDays(1);

		// Create the initial query
		Query query = new Query(this.query);
		query.setCount(MAX_COUNT);
		query.setSince(formatDateTime(earliestTime));
		query.setUntil(formatDateTime(upperBound));

		// Execute the queries
		QueryResult result = null;
		List<Status> tweets = new ArrayList<Status>();
		while(result == null || result.hasNext()) {
			// Execute the current query
			result = twitter.search(query);
			for(Status tweet : result.getTweets()) {
				// If the tweet is too early, stop looking
				if(new DateTime(tweet.getCreatedAt()).isBefore(earliestTime)) {
					return tweets;
				}
				
				// Filter the tweet and add it to the result
				if(intervalFilter.filter(tweet)) {
					tweets.add(tweet);
				}
			}
			
			// Get the next query
			if(result.hasNext()) {
				query = result.nextQuery();
			}
		}

		return tweets;
	}
}
