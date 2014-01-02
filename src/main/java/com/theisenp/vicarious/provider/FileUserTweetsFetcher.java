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
	 */
	public FileUserTweetsFetcher(String user, File lastTweetTime) {
		super(user, readTime(lastTweetTime));
	}

	/**
	 * @param lastTweetTime
	 * A file containing the timestamp of the last handled tweet
	 * @return The time stored in the given file, or the default time if the
	 * file does not exist
	 */
	private static DateTime readTime(File lastTweetTime) {
		// If the file doesn't exist, return the default early time
		if(!lastTweetTime.exists()) {
			return DEFAULT_EARLIEST;
		}

		// Read the time from the file
		try {
			String text = FileUtils.readFileToString(lastTweetTime);
			long lastTweetTimeMillis = Long.valueOf(text);
			return new DateTime(lastTweetTimeMillis + 1);
		}
		catch(IOException exception) {
			return DEFAULT_EARLIEST;
		}
	}
}
