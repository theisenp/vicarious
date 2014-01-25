Vicarious
=========

Vicarious is a library for automatically fetching tweets, optionally modifying their contents, and then reposting them. Clients can use the default implementations for fetching, modifying, posting, and logging tweets or can provide their own implementations. Vicarious is based on the [Twitter4J][1] library.

Usage
-----

### Configuration ###

Because Vicarious depends on Twitter4J, clients must provide their Twitter API keys. There are a few different ways to do this, one of which is to store the information in a `twitter4j.properties` file in the project directory. More information on the various ways to configure Twitter4J can be found [here][2].

### Components ###

There are 4 main components of the Vicarious library:

1. `TweetProvider`
2. `TweetModifier`
3. `TweetPublisher`
4. `TweetLogger`

#### TweetProvider ####

`TweetProvider` is an interface for an object that supplies tweets. Implementations typically make use of a `TweetFetcher`, which retrieves tweets from Twitter via the Twitter4J API. Clients can provide their own implementation of `TweetFetcher` to focus and filter the results as they see fit.

This provider fetches all tweets from `@GeorgeMichael` that mention `@AnnVeal`:

```java
public class ExampleTweetFetcher extends FileUserTweetsFetcher {

	public ExampleTweetFetcher(File lastTweetTime) throws IOException {
		super("GeorgeMichael", lastTweetTime);
	}

	@Override
	public List<Status> fetch(Twitter twitter) throws TwitterException {
		List<Status> result = new ArrayList<Status>();
		TweetFilter annFilter = new AnnFilter();

		for(Status tweet : super.fetch(twitter)) {
			if(annFilter.filter(tweet)) {
				result.add(tweet);
			}
		}

		return result;
	}

	private static class AnnFilter implements TweetFilter {
		@Override
		public boolean filter(Status tweet) {
			return tweet.getText().contains("@AnnVeal");
		}
	}
}	
```

#### TweetModifier ####

`TweetModifier` is an interface for an object that modifies or responds to tweets. Clients provide their own implementations to transform or respond to the tweets produced by their `TweetProvider` implementation.

This modifier responds to the tweets from `@GeorgeMichael` with "Her?":

```java
public class ExampleTweetModifier extends BaseTweetReplyModifier {

	@Override
	protected String respond(String user, String text) {
		return "Her?";
	}
}
```

#### TweetPublisher ####

As the name suggests, `TweetPublisher` is an interface for an object that publishes tweets. The default implementation of `BlockingTweetPublisher` should be sufficient for most applications.

#### TweetLogger ####

`TweetLogger` is an interface for an object that records tweets as they are handled. One useful implementation is `FileTweetLogger`, which records timestamps in a file. When used in conjunction with `FileTweetProvider`, it prevents Vicarious from posting duplicate tweets. A composite logger with console and file output should be sufficient for most applications.

### Execution ###

These four components can then be bundled together in an instance of `VicariousFactory` and passed to `VicariousRunner` to execute a single instance of the loop.

```java
public class ExampleVicariousFactory implements VicariousFactory {

	private static final String FILE_PATH = ".lastTweet";

	private final TweetProvider provider;
	private final TweetModifier modifier;
	private final TweetPublisher publisher;
	private final TweetLogger logger;
	
	public ExampleVicariousFactory() {
		Twitter twitter = TwitterFactory.getSingleton();
		File file = new File(FILE_PATH);

		// Create the provider
		TweetFetcher fetcher = new ExampleTweetFetcher(file);
		provider = new BaseTweetProvider(twitter, fetcher);

		// Create the modifier
		modifier = new ExampleTweetModifier();

		// Create the publisher
		publisher = new BlockingTweetPublisher(twitter);

		// Create the logger
		TweetLogger fileLogger = new FileTweetLogger(file);
		TweetLogger consoleLogger = new ConsoleTweetLogger();
		logger = new CompositeTweetLogger(fileLogger, consoleLogger);
	}

	@Override
	public TweetProvider getTweetProvider() {
		return provider;
	}

	@Override
	public TweetModifier getTweetModifier() {
		return modifier;
	}

	@Override
	public TweetPublisher getTweetPublisher() {
		return publisher;
	}

	@Override
	public TweetLogger getTweetLogger() {
		return logger;
	}
}
```

...


```java
public class Example {
	public static void main(String[] args) throws IOException {
		VicariousRunner.run(new ExampleVicariousFactory());
	}
}
```

_Note: Running single instances of the loop is ok for testing, but it's often more useful to schedule the Vicarious application to run regularly as a [cron][3] job._


Build
-----

Vicarious is built with [Maven][4]. To build the project and install it in your local Maven repositiory, type:

	mvn clean install

Download
--------

To include Vicarious in another Maven project, add the following dependency to the project's `pom.xml`:

```xml
<dependency>
  <groupId>com.theisenp.vicarious</groupId>
  <artifactId>vicarious</artifactId>
  <version>(latest version)</version>
</dependency>
```

Developed By
------------

* Patrick Theisen - <theisenp@gmail.com>

License
-------

    Copyright 2014 Patrick Theisen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: http://twitter4j.org/en/index.html
[2]: http://twitter4j.org/en/configuration.html
[3]: http://en.wikipedia.org/wiki/Cron
[4]: http://maven.apache.org/