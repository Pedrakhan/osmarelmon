package rss;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import util.Constants;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * This class creates rss feeds and feed entries.
 * 
 * @author sebastian
 * 
 */
public class RSSFactory {
	private static final Log log = LogFactory.getLog(RSSFactory.class);
	private static GregorianCalendar cal;

	private RSSFactory() {
	}

	public static SyndFeed createNewRSSFeed(String feedTitle) {
		SyndFeed feed = new SyndFeedImpl();
		List<?> entries = new LinkedList<Object>();

		feed.setFeedType("rss_2.0");
		feed.setTitle(feedTitle);
		feed.setLink("http://osmarelmon.won2.de/Feed?action=" + feedTitle);
		feed.setDescription("This feed has been created using ROME (Java syndication utilities)");
		feed.setEntries(entries);
		return feed;
	}

	/**
	 * Adds a new message to the given feed.
	 * 
	 * @param feed
	 *            The feed where the message has to be added.
	 * @param title
	 *            The title of the message.
	 * @param desc
	 *            The description of the message.
	 * @return {@code false} Always - its actually unused, one can change the
	 *         return type to void.
	 */
	public static boolean addNewMessageToFeed(SyndFeed feed, String title,
			String desc) {
		cal = new GregorianCalendar();
		List<SyndEntry> entries = feed.getEntries();
		if (entries.size() > util.Constants.MAX_RSS_MESSAGES) {
			log.info("Deleting first " + util.Constants.DELETE_FIRST_X_MESSAGES
					+ " from feed " + feed.getTitle() + ".");

			int counter = 0;
			for (Iterator<SyndEntry> it = entries.iterator(); it.hasNext()
					&& counter < Constants.DELETE_FIRST_X_MESSAGES; counter++) {
				entries.remove(counter);
			}
		}
		SyndEntry entry;
		SyndContent description;

		entry = new SyndEntryImpl();
		entry.setTitle(title);
		// entry.setLink(feed.getLink());
		entry.setPublishedDate(cal.getTime());

		description = new SyndContentImpl();
		description.setType("text/plain");
		description.setValue(desc);
		entry.setDescription(description);
		entries.add(entry);
		return false;
	}
}
