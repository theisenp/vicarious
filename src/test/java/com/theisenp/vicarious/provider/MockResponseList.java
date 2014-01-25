package com.theisenp.vicarious.provider;

import java.util.ArrayList;

import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;

/**
 * Empty extension of {@link ResponseList} backed by an {@link ArrayList}
 * 
 * @param <T>
 * @author patrick.theisen
 */
public class MockResponseList<T> extends ArrayList<T> implements
		ResponseList<T> {

	// Constants
	private static final long serialVersionUID = 1L;

	@Override
	public int getAccessLevel() {
		return 0;
	}

	@Override
	public RateLimitStatus getRateLimitStatus() {
		return null;
	}
}
