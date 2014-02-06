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
import com.theisenp.vicarious.provider.filters.IgnoreRepliesFilter;
import com.theisenp.vicarious.provider.filters.IgnoreRetweetsFilter;
import com.theisenp.vicarious.provider.filters.LatestTimeFilter;

/**
 * Extension of {@link IntervalTweetFetcher} that retrieves tweets based on a
 * {@link Query}
 * 
 * @author patrick.theisen
 */
public class QueryTweetFetcher implements IntervalTweetFetcher {

	// Constants
	private static final int MAX_COUNT = 100;

	// Data
	private final String query;

	/**
	 * @param query
	 * The query string
	 */
	public QueryTweetFetcher(String query) {
		this.query = query;
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
		QueryResult queryResult = null;
		List<Status> result = new ArrayList<Status>();
		while(queryResult == null || queryResult.hasNext()) {
			queryResult = twitter.search(query);
			List<Status> tweets = queryResult.getTweets();

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

			// Get the next query
			if(queryResult.hasNext()) {
				query = queryResult.nextQuery();
			}
		}

		return result;
	}
}
