package com.theisenp.vicarious.logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Implementation of {@link TweetLogger} that wraps an arbitrary number of other
 * loggers
 * 
 * @author patrick.theisen
 */
public class CompositeTweetLogger implements TweetLogger {

	// Data
	private final List<TweetLogger> delegates;

	/**
	 * @param delegates
	 * A list of filters to combine
	 */
	public CompositeTweetLogger(TweetLogger... delegates) {
		this(Arrays.asList(delegates));
	}

	/**
	 * @param delegates
	 * A collection of filters to combine
	 */
	public CompositeTweetLogger(Collection<TweetLogger> delegates) {
		this.delegates = new ArrayList<TweetLogger>(delegates);
	}

	@Override
	public void log(Status original, StatusUpdate response) {
		for(TweetLogger delegate : delegates) {
			delegate.log(original, response);
		}
	}
}
