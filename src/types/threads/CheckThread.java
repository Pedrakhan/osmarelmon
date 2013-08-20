package types.threads;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import rss.RSSFeedManager;
import util.FileManager;

/**
 * Thread to check the monitored relation. If you start the thread the checking
 * will bei triggered.
 * 
 * @author sebastian
 * 
 */
public class CheckThread extends Thread {

	private static final Log log = LogFactory.getLog(CheckThread.class);

	public CheckThread() {
		super();
	}

	@Override
	public void run() {
		FileManager man = FileManager.getInstance();
		while (true) {
			log.info("Initialized relation checking");
			RSSFeedManager.seekAndDestroyUnusedFeeds();
			if (log.isDebugEnabled()) {
				RSSFeedManager.generateRSSEntry(util.Constants.DEBUG_FEED,
						"Working", "CheckThread is going to work.");
			}
			man.triggerCheckingForMonitoredRelations();
			try {
				sleep(util.Constants.REFRESH_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
