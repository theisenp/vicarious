package com.theisenp.vicarious.provider;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Implementation of {@link TweetFetcher} that uses a timestamp file and a
 * delegate {@link IntervalTweetFetcher} to fetch tweets
 * 
 * @author patrick.theisen
 */
public class FileIntervalTweetFetcher implements TweetFetcher {

	// Data
	private final DateTime earliestTime;
	private final IntervalTweetFetcher delegate;

	/**
	 * @param lastTweetTime
	 * A file containing the timestamp of the last handled tweet
	 * @throws IOException
	 */
	public FileIntervalTweetFetcher(File lastTweetTime,
			IntervalTweetFetcher delegate) throws IOException {
		this.earliestTime = readTime(lastTweetTime);
		this.delegate = delegate;
	}

	@Override
	public List<Status> fetch(Twitter twitter) throws TwitterException {
		return delegate.fetch(twitter, earliestTime);
	}

	/**
	 * @param lastTweetTime
	 * A file containing the timestamp of the last handled tweet
	 * @return The time stored in the given file, or the default time if the
	 * file does not exist
	 * @throws IOException
	 */
	private static DateTime readTime(File lastTweetTime) throws IOException {
		// If the file doesn't exist, return the default early time
		if(!lastTweetTime.exists()) {
			return IntervalTweetFetcher.DEFAULT_EARLIEST;
		}

		// Read the time from the file
		String text = FileUtils.readFileToString(lastTweetTime);
		long lastTweetTimeMillis = Long.valueOf(text);
		return new DateTime(lastTweetTimeMillis + 1);
	}
}
