package rss;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.FileManager;

import com.sun.syndication.feed.synd.SyndFeed;

/**
 * This class manages the rss feeds.
 * 
 * @author sebastian
 * 
 */
public class RSSFeedManager {

	private static HashMap<String, SyndFeed> rssFeeds;
	private static final Log log = LogFactory.getLog(RSSFeedManager.class);

	private RSSFeedManager() {
	}

	static {
		rssFeeds = new HashMap<String, SyndFeed>();
		generateRSSEntry(util.Constants.OSMARELMON_STANDARD_FEED,
				"Startup message", "Everything is fine over here");
	}

	/**
	 * Generates a new RSS entry. If the rss feed which corresponds with the
	 * query name is not available, this feed will be generated, too.
	 * 
	 * @param feedTitle
	 *            The name of the feed.
	 * @param title
	 *            The title of the message.
	 * @param description
	 *            The description of the rss entry.
	 * 
	 * @return {@code true} if the rss message was successfully created.
	 */
	public static boolean generateRSSEntry(String feedTitle, String title,
			String description) {
		SyndFeed feed = null;
		boolean success = false;

		if (!rssFeeds.containsKey(feedTitle)) {
			feed = RSSFactory.createNewRSSFeed(feedTitle);
			feed.setLastVisited(new Timestamp(System.currentTimeMillis()));
			rssFeeds.put(feedTitle, feed);
		}

		feed = rssFeeds.get(feedTitle);
		success = RSSFactory.addNewMessageToFeed(feed, title, description);
		return success;
	}

	/**
	 * Returns the requested rss feed.
	 * 
	 * @param feedTitle
	 *            The title of the rss feed.
	 * @return the requested feed or null if there is no feed with the specified
	 *         title
	 */
	public static SyndFeed getRSSFeed(String feedTitle) {
		SyndFeed feed = rssFeeds.get(feedTitle);
		if (feed != null) {
			feed.setLastVisited(new Timestamp(System.currentTimeMillis()));
		}
		return feed;
	}

	/**
	 * Starts RSS feeds for existing myosm objects again, for example after
	 * server shutdown.
	 */
	public static void startRSSFeedsForExistingMyOSMs() {
		log.info("Setting up RSS feeds for all existing .myosm files.");
		FileManager man = FileManager.getInstance();
		List<String> monRels = man.getMonitoredRelations();
		for (String name : monRels) {
			generateRSSEntry(name, "Started feed for " + name,
					"Started feed again after server shutdown.");
		}
	}

	/**
	 * Looks for feeds which were not visited by users since a specified time
	 * period. How long this period is, can be seen in
	 * util.Constants.MAX_RSS_NOT_VISITED.
	 */
	public static void seekAndDestroyUnusedFeeds() {
		List<String> toBeDeleted = new LinkedList<String>();
		for (Entry<String, SyndFeed> entry : rssFeeds.entrySet()) {
			SyndFeed feed = entry.getValue();
			Timestamp stmp = feed.getLastVisited();
			long timeNow = System.currentTimeMillis();
			long duration = timeNow - stmp.getTime();
			if (duration > util.Constants.MAX_RSS_NOT_VISITED
					&& !entry.getKey().equals(
							util.Constants.OSMARELMON_STANDARD_FEED)
					&& !entry.getKey().equals(util.Constants.DEBUG_FEED)) {
				log.info("Found not visited RSS feed: " + entry.getKey());
				toBeDeleted.add(entry.getKey());
			}
		}
		for (String feedTitle : toBeDeleted) {
			rssFeeds.remove(feedTitle);
			FileManager man = FileManager.getInstance();
			try {
				man.deleteFile(util.Constants.HOME_DIR + feedTitle
						+ util.Constants.FILE_ENDING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.info("Deleted RSS feed for " + feedTitle);
		}
	}
}
