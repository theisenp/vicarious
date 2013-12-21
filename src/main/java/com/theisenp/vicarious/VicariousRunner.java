package com.theisenp.vicarious;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.StatusUpdate;

import com.theisenp.vicarious.modifier.TweetModifier;
import com.theisenp.vicarious.provider.TweetProvider;
import com.theisenp.vicarious.publisher.TweetPublisher;

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
		List<Status> originalTweets = provider.getTweets();
		if(originalTweets == null || originalTweets.isEmpty()) {
			return;
		}

		// Modify the tweets
		TweetModifier modifier = factory.getTweetModifier();
		List<StatusUpdate> modifiedTweets = new ArrayList<StatusUpdate>();
		for(Status originalTweet : originalTweets) {
			StatusUpdate modifiedTweet = modifier.modify(originalTweet);
			if(modifiedTweet != null) {
				modifiedTweets.add(modifiedTweet);
			}
		}

		// Publish the tweets
		TweetPublisher publisher = factory.getTweetPublisher();
		for(StatusUpdate modifiedTweet : modifiedTweets) {
			publisher.publish(modifiedTweet);
		}
	}
}
