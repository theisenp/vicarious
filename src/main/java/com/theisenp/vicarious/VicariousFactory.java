package com.theisenp.vicarious;

import com.theisenp.vicarious.modifier.TweetModifier;
import com.theisenp.vicarious.provider.TweetProvider;
import com.theisenp.vicarious.publisher.TweetPublisher;

/**
 * Interface for an object that provides the various components of the vicarious
 * system
 * 
 * @author patrick.theisen
 */
public interface VicariousFactory {

	/**
	 * @return An instance of {@link TweetProvider}
	 */
	public TweetProvider getTweetProvider();

	/**
	 * @return An instance of {@link TweetModifier}
	 */
	public TweetModifier getTweetModifier();

	/**
	 * @return An instance of {@link TweetPublisher}
	 */
	public TweetPublisher getTweetPublisher();
}
