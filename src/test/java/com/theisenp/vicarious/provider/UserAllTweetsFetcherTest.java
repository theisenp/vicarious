package com.theisenp.vicarious.provider;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;

import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import com.theisenp.vicarious.provider.UserAllTweetsFetcher;

/**
 * Unit tests for {@link UserAllTweetsFetcher}
 * 
 * @author patrick.theisen
 */
@SuppressWarnings("unchecked")
public class UserAllTweetsFetcherTest {

	// Constants
	private static final String TEST_USER = "user";

	@Test
	public void testFetchNoTweets() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

		ResponseList<Status> emptyPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(emptyPage);

		assertThat(fetcher.fetch(twitter)).isEmpty();
	}

	@Test
	public void testFetchSinglePage() throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

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
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

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
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

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
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

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
	public void testFetchTwitterUnavailableImmediately()
			throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

		// Throw an exception when asked for the user timeline
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenThrow(mock(TwitterException.class));

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).isEmpty();
	}

	@Test
	public void testFetchTwitterUnavailableAfterFirstPage()
			throws TwitterException {
		Twitter twitter = mock(Twitter.class);
		UserAllTweetsFetcher fetcher = new UserAllTweetsFetcher(TEST_USER);

		// Provide a single page of tweets before throwing an exception
		ResponseList<Status> firstPage = new MockResponseList<Status>();
		when(twitter.getUserTimeline(anyString(), any(Paging.class)))
				.thenReturn(firstPage).thenThrow(mock(TwitterException.class));

		// Fill the page
		for(int i = 0; i < 100; i++) {
			Status tweet = mockTweet();
			firstPage.add(tweet);
		}

		List<Status> result = fetcher.fetch(twitter);
		assertThat(result).hasSize(100).containsOnly(firstPage.toArray());
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
