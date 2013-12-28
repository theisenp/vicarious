package com.theisenp.vicarious.logger;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import twitter4j.Status;

/**
 * Unit tests for {@link FileTweetLogger}
 * 
 * @author patrick.theisen
 */
public class FileTweetLoggerTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	// Constants
	private static final String TEST_FILE_PATH = "file-tweet-logger-test";

	// Data
	private File file;

	@Before
	public void setUp() {
		file = new File(TEST_FILE_PATH);
		file.deleteOnExit();
	}

	@After
	public void tearDown() {
		file.delete();
	}

	@Test
	public void testLogSingleTweet() throws IOException {
		TweetLogger logger = new FileTweetLogger(file);

		Status tweet = mock(Status.class);
		long tweetTime = 12345L;
		when(tweet.getCreatedAt()).thenReturn(new Date(tweetTime));
		logger.log(tweet, null);

		String text = FileUtils.readFileToString(file);
		assertThat(text).isEqualTo(String.valueOf(tweetTime));
	}

	@Test
	public void testLogMultipleTweets() throws IOException {
		TweetLogger logger = new FileTweetLogger(file);

		Status tweet = mock(Status.class);
		long maxTime = 10L;
		for(long l = 0L; l <= maxTime; l++) {
			when(tweet.getCreatedAt()).thenReturn(new Date(l));
			logger.log(tweet, null);

			String text = FileUtils.readFileToString(file);
			assertThat(text).isEqualTo(String.valueOf(l));
		}

		String text = FileUtils.readFileToString(file);
		assertThat(text).isEqualTo(String.valueOf(maxTime));
	}

	@Test
	public void testLogFailure() throws IOException {
		TweetLogger logger = new FileTweetLogger(file);
		file.createNewFile();
		file.setReadOnly();

		Status tweet = mock(Status.class);
		long tweetTime = 12345L;
		when(tweet.getCreatedAt()).thenReturn(new Date(tweetTime));

		thrown.expect(RuntimeException.class);
		logger.log(tweet, null);
	}
}
