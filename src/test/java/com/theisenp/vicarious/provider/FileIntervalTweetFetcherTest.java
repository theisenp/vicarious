package com.theisenp.vicarious.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Unit tests for {@link FileUserTweetsFetcher}
 * 
 * @author patrick.theisen
 */
public class FileIntervalTweetFetcherTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	// Constants
	private static final String TEST_FILE_PATH = ".test-output";

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
	public void testFileDoesNotExist() throws TwitterException, IOException {
		IntervalTweetFetcher delegate = mock(IntervalTweetFetcher.class);
		TweetFetcher fetcher = new FileIntervalTweetFetcher(file, delegate);

		Twitter twitter = mock(Twitter.class);
		fetcher.fetch(twitter);

		DateTime earliestTime = IntervalTweetFetcher.DEFAULT_EARLIEST;
		verify(delegate, times(1)).fetch(twitter, earliestTime);
	}

	@Test
	public void testFileCannotBeRead() throws TwitterException, IOException {
		file.createNewFile();
		file.setReadable(false);

		thrown.expect(IOException.class);
		IntervalTweetFetcher delegate = mock(IntervalTweetFetcher.class);
		new FileIntervalTweetFetcher(file, delegate);
	}

	@Test
	public void testFile() throws TwitterException, IOException {
		DateTime lastTweetTime = new DateTime();
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(String.valueOf(lastTweetTime.getMillis()).getBytes());
		stream.close();

		IntervalTweetFetcher delegate = mock(IntervalTweetFetcher.class);
		TweetFetcher fetcher = new FileIntervalTweetFetcher(file, delegate);

		Twitter twitter = mock(Twitter.class);
		fetcher.fetch(twitter);

		DateTime earliestTime = lastTweetTime.plusMillis(1);
		verify(delegate, times(1)).fetch(twitter, earliestTime);
	}
}
