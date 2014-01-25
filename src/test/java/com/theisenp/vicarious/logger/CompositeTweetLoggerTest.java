package com.theisenp.vicarious.logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Unit tests for {@link CompositeTweetLogger}
 * 
 * @author patrick.theisen
 */
public class CompositeTweetLoggerTest {

	@Test
	public void testNoLoggers() {
		TweetLogger composite = new CompositeTweetLogger();
		Status original = mock(Status.class);
		StatusUpdate response = new StatusUpdate("");

		composite.log(original, response);
	}

	@Test
	public void testSingleLogger() {
		TweetLogger delegate = mock(TweetLogger.class);
		TweetLogger composite = new CompositeTweetLogger(delegate);
		Status original = mock(Status.class);
		StatusUpdate response = new StatusUpdate("");

		composite.log(original, response);

		verify(delegate, times(1)).log(original, response);
	}

	@Test
	public void testMultipleLoggers() {
		TweetLogger first = mock(TweetLogger.class);
		TweetLogger second = mock(TweetLogger.class);
		TweetLogger composite = new CompositeTweetLogger(first, second);
		Status original = mock(Status.class);
		StatusUpdate response = new StatusUpdate("");

		composite.log(original, response);

		verify(first, times(1)).log(original, response);
		verify(second, times(1)).log(original, response);
	}
}
