package com.theisenp.vicarious.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import twitter4j.Status;
import twitter4j.StatusUpdate;

/**
 * Implementation of {@link TweetLogger} that writes data to a file
 * 
 * @author patrick.theisen
 */
public class FileTweetLogger implements TweetLogger {

	// Data
	private final File file;

	/**
	 * @param file
	 * The file to write data to
	 */
	public FileTweetLogger(File file) {
		this.file = file;
	}

	@Override
	public void log(Status original, StatusUpdate response) {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			long millis = original.getCreatedAt().getTime();
			stream.write(String.valueOf(millis).getBytes());
		}
		catch(IOException exception) {
			throw new RuntimeException(exception);
		}
		finally {
			IOUtils.closeQuietly(stream);
		}
	}
}
