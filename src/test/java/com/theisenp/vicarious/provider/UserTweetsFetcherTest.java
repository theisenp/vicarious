package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Unit tests for {@link UserTweetsFetcher}
 * 
 * @author patrick.theisen
 */
@SuppressWarnings("unchecked")
public class UserTweetsFetcherTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	// Constants
	private static final String TEST_USER = "user";
	private static final DateTime TEST_TIME_EARLY = new DateTime(0);
	private static final DateTime TEST_TIME_EXACT = new DateTime(10);
	private static final DateTime TEST_TIME_LATE = new DateTime(100);

	@Test
	public void testFetchNoTweets() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(emptyPage);

		assertThat(fetcher.fetch(twitter)).isEmpty();
	}

	@Test
	public void testFetchSinglePage() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		// Return a single page of tweets
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, emptyPage);

		// Fill the page
		for(int i = 0; i < 100; i++) {
			Status tweet = mockTweet();
			firstPage.add(tweet);
		}

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(100).containsOnly(firstPage.toArray());
	}

	@Test
	public void testFetchMultiplePages() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		// Split tweets across multiple pages
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> secondPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		ResponseList<Status> allTweets = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, secondPage, emptyPage);

		// Fill the pages
		for(int i = 0; i < 100; i++) {
			Status firstTweet = mockTweet();
			firstPage.add(firstTweet);
			allTweets.add(firstTweet);

			Status secondTweet = mockTweet();
			secondPage.add(secondTweet);
			allTweets.add(secondTweet);
		}

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(200).containsOnly(allTweets.toArray());
	}

	@Test
	public void testFetchMultiplePagesWithRetweets() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		// Split tweets across multiple pages
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> secondPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		ResponseList<Status> allTweets = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, secondPage, emptyPage);

		// Fill the pages
		for(int i = 0; i < 100; i++) {
			boolean even = 1 % 2 == 0;
			Status firstTweet = even ? mockTweet() : mockRetweet();
			firstPage.add(firstTweet);
			if(even) {
				allTweets.add(firstTweet);
			}

			Status secondTweet = !even ? mockTweet() : mockRetweet();
			secondPage.add(secondTweet);
			if(!even) {
				allTweets.add(secondTweet);
			}
		}

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(100).containsOnly(allTweets.toArray());
	}

	@Test
	public void testFetchMultiplePagesWithReplies() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		// Split tweets across multiple pages
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> secondPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		ResponseList<Status> allTweets = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, secondPage, emptyPage);

		// Fill the pages
		for(int i = 0; i < 100; i++) {
			boolean even = 1 % 2 == 0;
			Status firstTweet = even ? mockTweet() : mockReply();
			firstPage.add(firstTweet);
			if(even) {
				allTweets.add(firstTweet);
			}

			Status secondTweet = !even ? mockTweet() : mockReply();
			secondPage.add(secondTweet);
			if(!even) {
				allTweets.add(secondTweet);
			}
		}

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(100).containsOnly(allTweets.toArray());
	}

	@Test
	public void testFetchEarlyTweets() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher =
				new UserTweetsFetcher(TEST_USER, TEST_TIME_EXACT);

		// Return a single page of tweets
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage).thenThrow(new IllegalStateException());

		// Fill the page
		Status exactTweet = mockTweet();
		Status earlyTweet = mockTweet();
		when(exactTweet.getCreatedAt()).thenReturn(TEST_TIME_EXACT.toDate());
		when(earlyTweet.getCreatedAt()).thenReturn(TEST_TIME_EARLY.toDate());
		firstPage.add(exactTweet);
		firstPage.add(earlyTweet);

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(1).containsOnly(exactTweet);
	}

	@Test
	public void testFetchLateTweets() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher =
				new UserTweetsFetcher(TEST_USER, TEST_TIME_EXACT,
						TEST_TIME_EXACT);

		// Return a single page of tweets
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage, emptyPage);

		// Fill the page
		Status lateTweet = mockTweet();
		Status exactTweet = mockTweet();
		when(lateTweet.getCreatedAt()).thenReturn(TEST_TIME_LATE.toDate());
		when(exactTweet.getCreatedAt()).thenReturn(TEST_TIME_EXACT.toDate());
		firstPage.add(lateTweet);
		firstPage.add(exactTweet);

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(1).containsOnly(exactTweet);
	}

	@Test
	public void testFetchEarlyAndLateTweets() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher =
				new UserTweetsFetcher(TEST_USER, TEST_TIME_EXACT,
						TEST_TIME_EXACT);

		// Return a single page of tweets
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage).thenThrow(new IllegalStateException());

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

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(1).containsOnly(exactTweet);
	}

	@Test
	public void testFetchTwitterUnavailableImmediately()
			throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		// Throw an exception when asked for the user timeline
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenThrow(mock(TwitterException.class));

		thrown.expect(TwitterException.class);
		fetcher.fetch(twitter);
	}

	@Test
	public void testFetchTwitterUnavailableAfterFirstPage()
			throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserTweetsFetcher fetcher = new UserTweetsFetcher(TEST_USER);

		// Provide a single page of tweets before throwing an exception
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage).thenThrow(mock(TwitterException.class));

		// Fill the page
		for(int i = 0; i < 100; i++) {
			Status tweet = mockTweet();
			firstPage.add(tweet);
		}

		thrown.expect(TwitterException.class);
		fetcher.fetch(twitter);
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

	/**
	 * @return A tweet retweeted by the user
	 */
	private static Status mockRetweet() {
		Status response = mock(Status.class);

		when(response.isRetweet()).thenReturn(true);
		when(response.getInReplyToUserId()).thenReturn(-1L);
		when(response.getInReplyToStatusId()).thenReturn(-1L);
		when(response.getInReplyToScreenName()).thenReturn(null);

		return response;
	}

	/**
	 * @return A tweet in reply to another user
	 */
	private static Status mockReply() {
		Status response = mock(Status.class);

		when(response.isRetweet()).thenReturn(false);
		when(response.getInReplyToUserId()).thenReturn(1L);
		when(response.getInReplyToStatusId()).thenReturn(1L);
		when(response.getInReplyToScreenName()).thenReturn("other");

		return response;
	}
}
