package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Unit tests for {@link FileUserTweetsFetcher}
 * 
 * @author patrick.theisen
 */
@SuppressWarnings("unchecked")
public class FileUserTweetsFetcherTest {

	// Constants
	private static final String USER = "test";
	private static final DateTime TEST_TIME_EARLY = new DateTime(0);
	private static final DateTime TEST_TIME_EXACT = new DateTime(1);
	private static final DateTime TEST_TIME_LATE = new DateTime(2);
	private static final String TEST_FILE_PATH =
			"file-user-tweets-fetcher-test";

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
	public void testFileDoesNotExist() throws TwitterException {
		// Return a single page of tweets
		Twitter twitter = mock(Twitter.class);
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, emptyPage);

		// Fill the page
		Status lateTweet = mockTweet();
		Status exactTweet = mockTweet();
		Status earlyTweet = mockTweet();
		when(lateTweet.getCreatedAt()).thenReturn(TEST_TIME_LATE.toDate());
		when(exactTweet.getCreatedAt()).thenReturn(TEST_TIME_EXACT.toDate());
		when(earlyTweet.getCreatedAt()).thenReturn(TEST_TIME_EARLY.toDate());
		firstPage.add(lateTweet);
		firstPage.add(exactTweet);
		firstPage.add(earlyTweet);

		TweetFetcher fetcher = new FileUserTweetsFetcher(USER, file);
		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(3).containsOnly(firstPage.toArray());
	}

	@Test
	public void testFileCannotBeRead() throws TwitterException, IOException {
		file.createNewFile();
		file.setReadable(false);

		// Return a single page of tweets
		Twitter twitter = mock(Twitter.class);
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, emptyPage);

		// Fill the page
		Status lateTweet = mockTweet();
		Status exactTweet = mockTweet();
		Status earlyTweet = mockTweet();
		when(lateTweet.getCreatedAt()).thenReturn(TEST_TIME_LATE.toDate());
		when(exactTweet.getCreatedAt()).thenReturn(TEST_TIME_EXACT.toDate());
		when(earlyTweet.getCreatedAt()).thenReturn(TEST_TIME_EARLY.toDate());
		firstPage.add(lateTweet);
		firstPage.add(exactTweet);
		firstPage.add(earlyTweet);

		TweetFetcher fetcher = new FileUserTweetsFetcher(USER, file);
		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(3).containsOnly(firstPage.toArray());
	}

	@Test
	public void testFile() throws TwitterException, IOException {
		FileOutputStream stream = new FileOutputStream(file);
		String millis = String.valueOf(TEST_TIME_EXACT.getMillis());
		stream.write(millis.getBytes());
		stream.close();

		// Return a single page of tweets
		Twitter twitter = mock(Twitter.class);
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, emptyPage);

		// Fill the page
		Status lateTweet = mockTweet();
		Status exactTweet = mockTweet();
		Status earlyTweet = mockTweet();
		when(lateTweet.getCreatedAt()).thenReturn(TEST_TIME_LATE.toDate());
		when(exactTweet.getCreatedAt()).thenReturn(TEST_TIME_EXACT.toDate());
		when(earlyTweet.getCreatedAt()).thenReturn(TEST_TIME_EARLY.toDate());
		firstPage.add(lateTweet);
		firstPage.add(exactTweet);
		firstPage.add(earlyTweet);

		TweetFetcher fetcher = new FileUserTweetsFetcher(USER, file);
		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(1).containsOnly(lateTweet);
	}

	/**
	 * @return An tweet authored by the user
	 */
	private static Status mockTweet() {
		Status response = mock(Status.class);

		when(response.isRetweet()).thenReturn(false);
		when(response.getInReplyToUserId()).thenReturn(-1L);
		when(response.getInReplyToStatusId()).thenReturn(-1L);
		when(response.getInReplyToScreenName()).thenReturn(null);

		return response;
	}
}
