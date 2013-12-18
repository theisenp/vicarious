package com.theisenp.vicarious.provider.filters;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.theisenp.vicarious.provider.TweetFilter;
import com.theisenp.vicarious.provider.filters.CompositeTweetFilter;

import twitter4j.Status;

/**
 * Unit tests for {@link CompositeTweetFilter}
 * 
 * @author patrick.theisen
 */
public class CompositeTweetFilterTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void testNoFilters() {
		CompositeTweetFilter filter = new CompositeTweetFilter();

		Status tweet = mock(Status.class);
		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testSingleFilterFail() {
		TweetFilter failFilter = mock(TweetFilter.class);
		when(failFilter.filter(any(Status.class))).thenReturn(false);
		CompositeTweetFilter filter = new CompositeTweetFilter(failFilter);

		Status tweet = mock(Status.class);
		assertThat(filter.filter(tweet)).isFalse();
	}

	@Test
	public void testSingleFilterPass() {
		TweetFilter failFilter = mock(TweetFilter.class);
		when(failFilter.filter(any(Status.class))).thenReturn(true);
		CompositeTweetFilter filter = new CompositeTweetFilter(failFilter);

		Status tweet = mock(Status.class);
		assertThat(filter.filter(tweet)).isTrue();
	}

	@Test
	public void testMultipleFilterFailAll() {
		TweetFilter firstFailFilter = mock(TweetFilter.class);
		TweetFilter secondFailFilter = mock(TweetFilter.class);
		when(firstFailFilter.filter(any(Status.class))).thenReturn(false);
		when(secondFailFilter.filter(any(Status.class))).thenReturn(false);
		CompositeTweetFilter filter =
				new CompositeTweetFilter(firstFailFilter, secondFailFilter);

		Status tweet = mock(Status.class);
		assertThat(filter.filter(tweet)).isFalse();
	}

	@Test
	public void testMultipleFilterFailOne() {
		TweetFilter passFilter = mock(TweetFilter.class);
		TweetFilter failFilter = mock(TweetFilter.class);
		when(passFilter.filter(any(Status.class))).thenReturn(true);
		when(failFilter.filter(any(Status.class))).thenReturn(false);
		CompositeTweetFilter filter =
				new CompositeTweetFilter(passFilter, failFilter);

		Status tweet = mock(Status.class);
		assertThat(filter.filter(tweet)).isFalse();
	}

	@Test
	public void testMultipleFilterPass() {
		TweetFilter firstPassFilter = mock(TweetFilter.class);
		TweetFilter secondPassFilter = mock(TweetFilter.class);
		when(firstPassFilter.filter(any(Status.class))).thenReturn(true);
		when(secondPassFilter.filter(any(Status.class))).thenReturn(true);
		CompositeTweetFilter filter =
				new CompositeTweetFilter(firstPassFilter, secondPassFilter);

		Status tweet = mock(Status.class);
		assertThat(filter.filter(tweet)).isTrue();
	}
}
