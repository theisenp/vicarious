package com.theisenp.vicarious.publisher;

import twitter4j.StatusUpdate;

/**
 * Interface with callbacks triggered by events in {@link TweetPublisher}
 * 
 * @author patrick.theisen
 */
public interface PublishSuccessListener {

	/**
	 * Called when a tweet has been published successfully
	 * 
	 * @param tweet
	 * The tweet that was published
	 */
	public void onPublishSuccess(StatusUpdate tweet);
}
