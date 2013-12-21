package com.theisenp.vicarious.provider.user;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.theisenp.vicarious.provider.TweetFetcher;
import com.theisenp.vicarious.provider.TweetFilter;
import com.theisenp.vicarious.provider.filters.CompositeTweetFilter;
import com.theisenp.vicarious.provider.filters.IgnoreRepliesFilter;
import com.theisenp.vicarious.provider.filters.IgnoreRetweetsFilter;

/**
 * Implementation of {@link TweetFetcher} that retrieves all tweets authored by
 * a particular user
 * 
 * @author patrick.theisen
 */
public class UserAllTweetsFetcher implements TweetFetcher {

	// Constants
	private static final int MAX_PAGE_SIZE = 100;
	private static final TweetFilter FILTER = new CompositeTweetFilter(
			new IgnoreRepliesFilter(), new IgnoreRetweetsFilter());

	// Data
	private final String user;

	/**
	 * @param user
	 * The user for whom to fetch tweets
	 */
	public UserAllTweetsFetcher(String user) {
		this.user = user;
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
	private static List<Status> filter(List<Status> unfilteredTweets) {
		List<Status> filteredTweets = new ArrayList<Status>();

		for(Status tweet : unfilteredTweets) {
			if(FILTER.filter(tweet)) {
				filteredTweets.add(tweet);
			}
		}

		return filteredTweets;
	}
}
