package com.theisenp.vicarious;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

import com.theisenp.vicarious.logger.TweetLogger;
import com.theisenp.vicarious.modifier.TweetModifier;
import com.theisenp.vicarious.provider.TweetProvider;
import com.theisenp.vicarious.publisher.PublishSuccessListener;
import com.theisenp.vicarious.publisher.TweetPublisher;
import com.theisenp.vicarious.util.TweetDateComparator;

/**
 * Static utility for running the vicarious loop
 * 
 * @author patrick.theisen
 */
public class VicariousRunner {

	/**
	 * Runs a single iteration of the provide, modify, publish loop using the
	 * implementations provided by the given factory
	 * 
	 * @param factory
	 * The implementations to use for the provide, modify, publish loop
	 */
	public static void run(VicariousFactory factory) {
		// Get the tweets
		TweetProvider provider = factory.getTweetProvider();
		List<Status> originals = null;
		try {
			originals = provider.getTweets();
			if(originals == null || originals.isEmpty()) {
				return;
			}
		}
		catch(TwitterException exception) {
			exception.printStackTrace();
			return;
		}

		// Sort the tweets from oldest to newest
		Comparator<Status> comparator = new TweetDateComparator();
		Collections.sort(originals, comparator);

		// Modify the tweets
		TweetModifier modifier = factory.getTweetModifier();
		List<TweetPair> tweetPairs = new ArrayList<TweetPair>();
		for(Status original : originals) {
			StatusUpdate response = modifier.modify(original);
			if(response != null) {
				tweetPairs.add(new TweetPair(original, response));
			}
		}

		// Publish the tweets
		TweetPublisher publisher = factory.getTweetPublisher();
		TweetLogger logger = factory.getTweetLogger();
		if(logger == null) {
			for(TweetPair tweetPair : tweetPairs) {
				publisher.publish(tweetPair.response);
			}
		}
		else {
			for(TweetPair tweetPair : tweetPairs) {
				publisher.publish(tweetPair.response, new LoggingListener(
						logger, tweetPair.original));
			}
		}
	}

	/**
	 * Struct holding an original tweet and the associated response
	 * 
	 * @author patrick.theisen
	 */
	private static class TweetPair {

		// Data
		public final Status original;
		public final StatusUpdate response;

		/**
		 * @param original
		 * The original tweet
		 * @param response
		 * The response tweet
		 */
		public TweetPair(Status original, StatusUpdate response) {
			this.original = original;
			this.response = response;
		}
	}

	/**
	 * Implementation of {@link PublishSuccessListener} that logs successful
	 * tweets
	 * 
	 * @author patrick.theisen
	 */
	private static class LoggingListener implements PublishSuccessListener {

		// Data
		private final TweetLogger logger;
		private final Status original;

		/**
		 * @param logger
		 * The logger to use
		 * @param original
		 * The original tweet associated with the expected response
		 */
		public LoggingListener(TweetLogger logger, Status original) {
			this.logger = logger;
			this.original = original;
		}

		@Override
		public void onPublishSuccess(StatusUpdate tweet) {
			logger.log(original, tweet);
		}

		@Override
		public void onPublishFailure(StatusUpdate tweet) {
		}
	}
}
