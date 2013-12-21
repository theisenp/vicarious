package com.theisenp.vicarious.publisher;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Implementation of TweetPublisher that publishes tweets synchronously
 * 
 * @author patrick.theisen
 */
public class BlockingTweetPublisher implements TweetPublisher {

	// Data
	private final Twitter twitter;

	/**
	 * @param twitter
	 * An instance of the Twitter API
	 */
	public BlockingTweetPublisher(Twitter twitter) {
		this.twitter = twitter;
	}

	@Override
	public void publish(StatusUpdate tweet) {
		publish(tweet, null);
	}

	@Override
	public void publish(StatusUpdate tweet, PublishSuccessListener listener) {
		try {
			twitter.updateStatus(tweet);
			if(listener != null) {
				listener.onPublishSuccess(tweet);
			}
		}
		catch(TwitterException exception) {
			if(listener != null) {
				listener.onPublishFailure(tweet);
			}
		}
	}
}
