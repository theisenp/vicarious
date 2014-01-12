package com.theisenp.vicarious.provider;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

/**
 * Extension of {@link UserTweetsFetcher} that reads the earliest allowed tweet
 * time from a file
 * 
 * @author patrick.theisen
 */
public class FileUserTweetsFetcher extends UserTweetsFetcher {

	/**
	 * @param user
	 * The user for whom to fetch tweets
	 * @param lastTweetTime
	 * A file containing the timestamp of the last handled tweet
	 * @throws IOException
	 */
	public FileUserTweetsFetcher(String user, File lastTweetTime)
			throws IOException {
		super(user, readTime(lastTweetTime));
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
			return DEFAULT_EARLIEST;
		}

		// Read the time from the file
		String text = FileUtils.readFileToString(lastTweetTime);
		long lastTweetTimeMillis = Long.valueOf(text);
		return new DateTime(lastTweetTimeMillis + 1);
	}
}
