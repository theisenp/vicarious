package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Unit tests for {@link QueryTweetFetcher}
 * 
 * @author patrick.theisen
 */
public class QueryTweetFetcherTest {

	// Constants
	private static final Throwable INVALID_PAGE = new RuntimeException(
			"Queried invalid page");

	@Test
	public void testSearchSinglePageEmpty() throws TwitterException {
		TweetFetcher fetcher = new QueryTweetFetcher("");

		// Mock the query result
		List<Status> tweets = new ArrayList<Status>();
		QueryResult result = mock(QueryResult.class);
		when(result.hasNext()).thenReturn(false);
		when(result.getTweets()).thenReturn(tweets).thenThrow(INVALID_PAGE);

		// Mock the twitter API
		Twitter twitter = mock(Twitter.class);
		when(twitter.search(any(Query.class))).thenReturn(result);

		assertThat(fetcher.fetch(twitter)).isEmpty();
	}

	@Test
	public void testSearchSinglePageOutsideBounds() throws TwitterException {
		DateTime early = new DateTime(100);
		TweetFetcher fetcher = new QueryTweetFetcher("", early);

		// Mock a page of results
		List<Status> firstPage = new ArrayList<Status>();
		for(int i = 1; i <= 100; i++) {
			firstPage.add(mockTweet(early.minusMillis(i)));
		}

		// Mock the query result
		QueryResult result = mock(QueryResult.class);
		when(result.hasNext()).thenReturn(false);
		when(result.getTweets()).thenReturn(firstPage).thenThrow(INVALID_PAGE);

		// Mock the twitter API
		Twitter twitter = mock(Twitter.class);
		when(twitter.search(any(Query.class))).thenReturn(result);

		assertThat(fetcher.fetch(twitter)).isEmpty();
	}

	@Test
	public void testSearchSinglePageInsideBounds() throws TwitterException {
		DateTime early = new DateTime(25);
		DateTime late = new DateTime(75);
		TweetFetcher fetcher = new QueryTweetFetcher("", early, late);

		// Mock a page of results
		List<Status> firstPage = new ArrayList<Status>();
		List<Status> expected = new ArrayList<Status>();
		for(int i = 100; i >= 1; i--) {
			Status tweet = mockTweet(new DateTime(i));
			firstPage.add(tweet);
			if(i >= 25 && i <= 75) {
				expected.add(tweet);
			}
		}

		// Mock the query result
		QueryResult result = mock(QueryResult.class);
		when(result.hasNext()).thenReturn(false);
		when(result.getTweets()).thenReturn(firstPage).thenThrow(INVALID_PAGE);

		// Mock the twitter API
		Twitter twitter = mock(Twitter.class);
		when(twitter.search(any(Query.class))).thenReturn(result);

		assertThat(fetcher.fetch(twitter)).isEqualTo(expected);
	}

	@Test
	public void testSearchMultiplePagesOutsideBounds() throws TwitterException {
		DateTime early = new DateTime(100);
		DateTime late = new DateTime(200);
		TweetFetcher fetcher = new QueryTweetFetcher("", early, late);

		// Mock two pages of results
		List<Status> firstPage = new ArrayList<Status>();
		List<Status> secondPage = new ArrayList<Status>();
		for(int i = 1; i <= 100; i++) {
			firstPage.add(mockTweet(late.plusMillis(i)));
			secondPage.add(mockTweet(early.minusMillis(i)));
		}

		// Mock the query result
		QueryResult firstResult = mock(QueryResult.class);
		QueryResult secondResult = mock(QueryResult.class);
		when(firstResult.hasNext()).thenReturn(true);
		when(secondResult.hasNext()).thenReturn(false);
		when(secondResult.nextQuery()).thenReturn(new Query());
		when(firstResult.getTweets()).thenReturn(firstPage).thenThrow(
				INVALID_PAGE);
		when(secondResult.getTweets()).thenReturn(secondPage).thenThrow(
				INVALID_PAGE);

		// Mock the twitter API
		Twitter twitter = mock(Twitter.class);
		when(twitter.search(any(Query.class))).thenReturn(firstResult)
				.thenReturn(secondResult);

		assertThat(fetcher.fetch(twitter)).isEmpty();
		verify(firstResult, times(1)).getTweets();
		verify(secondResult, times(1)).getTweets();
	}

	@Test
	public void testSearchMultiplePagesInsideBounds() throws TwitterException {
		DateTime early = new DateTime(50);
		DateTime late = new DateTime(150);
		TweetFetcher fetcher = new QueryTweetFetcher("", early, late);

		// Mock the first page of results
		List<Status> firstPage = new ArrayList<Status>();
		List<Status> expected = new ArrayList<Status>();
		for(int i = 200; i >= 101; i--) {
			Status tweet = mockTweet(new DateTime(i));
			firstPage.add(tweet);
			if(i <= 150) {
				expected.add(tweet);
			}
		}

		// Mock the second page of results
		List<Status> secondPage = new ArrayList<Status>();
		for(int i = 100; i >= 1; i--) {
			Status tweet = mockTweet(new DateTime(i));
			secondPage.add(tweet);
			if(i >= 50) {
				expected.add(tweet);
			}
		}

		// Mock the query result
		QueryResult firstResult = mock(QueryResult.class);
		QueryResult secondResult = mock(QueryResult.class);
		when(firstResult.hasNext()).thenReturn(true);
		when(secondResult.hasNext()).thenReturn(false);
		when(secondResult.nextQuery()).thenReturn(new Query());
		when(firstResult.getTweets()).thenReturn(firstPage).thenThrow(
				INVALID_PAGE);
		when(secondResult.getTweets()).thenReturn(secondPage).thenThrow(
				INVALID_PAGE);

		// Mock the twitter API
		Twitter twitter = mock(Twitter.class);
		when(twitter.search(any(Query.class))).thenReturn(firstResult)
				.thenReturn(secondResult);

		assertThat(fetcher.fetch(twitter)).isEqualTo(expected);
		verify(firstResult, times(1)).getTweets();
		verify(secondResult, times(1)).getTweets();
	}

	/**
	 * @param timestamp
	 * The time at which the tweet was created
	 * @return A status with the given timestamp
	 */
	private static Status mockTweet(DateTime timestamp) {
		Status result = mock(Status.class);
		when(result.getCreatedAt()).thenReturn(timestamp.toDate());
		return result;
	}
}
