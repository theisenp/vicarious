package com.theisenp.vicarious.provider;

import javax.net.ssl.SSLEngineResult.Status;

/**
 * Interface for an object that filters tweets
 * 
 * @author patrick.theisen
 */
public interface TweetFilter {

	/**
	 * @param tweet
	 * The tweet to check
	 * @return True if the given tweet passes the filter. Else, false.
	 */
	public boolean filter(Status tweet);
}
