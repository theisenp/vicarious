package com.theisenp.vicarious.util;

import java.util.Comparator;
import java.util.Date;

import twitter4j.Status;

/**
 * Implementation of {@link Comparator} that orders {@link Status} objects based
 * on their creation date
 * 
 * @author patrick.theisen
 */
public class TweetDateComparator implements Comparator<Status> {

	@Override
	public int compare(Status left, Status right) {
		Date leftDate = left.getCreatedAt();
		Date rightDate = right.getCreatedAt();
		return leftDate.compareTo(rightDate);
	}
}