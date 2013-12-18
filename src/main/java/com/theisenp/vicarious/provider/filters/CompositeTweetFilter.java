package com.theisenp.vicarious.provider.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.theisenp.vicarious.provider.TweetFilter;

import twitter4j.Status;

/**
 * Implementation of {@link TweetFilter} that wraps an arbitrary number of other
 * filters
 * 
 * @author patrick.theisen
 */
public class CompositeTweetFilter implements TweetFilter {

	// Data
	private final Collection<TweetFilter> filters;

	/**
	 * @param filters
	 * A list of filters to combine
	 */
	public CompositeTweetFilter(TweetFilter... filters) {
		this(Arrays.asList(filters));
	}

	/**
	 * @param filters
	 * A list of filters to combine
	 */
	public CompositeTweetFilter(Collection<TweetFilter> filters) {
		this.filters = new ArrayList<TweetFilter>(filters);
	}

	@Override
	public boolean filter(Status tweet) {
		for(TweetFilter filter : filters) {
			if(!filter.filter(tweet)) {
				return false;
			}
		}
		return true;
	}
}
