package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import parser.Parser;
import rss.RSSFeedManager;
import types.ChangedRelationContainer;
import types.MyOSM;
import types.OverpassQuery;
import types.threads.CheckThread;

/**
 * The FileManager deals with the stored .myosm data, i.e. stores and loads
 * them, and triggers the checking of the stored documents.
 * 
 * @author sebastian
 * 
 */
public final class FileManager {

	private static FileManager instance = null;
	private static final Log log = LogFactory.getLog(FileManager.class);

	private FileManager() {
	}

	static {
		CheckThread chThr = new CheckThread();
		chThr.start();
		RSSFeedManager.startRSSFeedsForExistingMyOSMs();
	}

	/**
	 * Returns the instance of the file manager.
	 * 
	 * @return instance of the file manager.
	 */
	public static synchronized FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}

	/**
	 * Adds a new relation to the monitor. If the result of the query contains
	 * no relations, the result is discarded.
	 * 
	 * @param strings
	 *            The key value pairs to define the relation.
	 * @return {@code true} if the relation was successfully added for
	 *         monitoring.
	 */
	public boolean addNewRelationToMonitor(List<String> strings, String name) {
		boolean success = false;
		String query = QueryBuilder.getRelationQuery(strings);
		String path = FileDownloader.download(query, name);
		MyOSM osm = null;
		try {
			osm = Parser.parseOSMFile(path, name);
			osm.setQuery(query);
			deleteFile(path);
			if (osm.getRelations().size() != 0) {
				success = saveOSM(osm, name);
			}
		} catch (Exception e) {
			log.error("File Parsing failed");
		}
		if (success) {
			rssAddMessage(name, "Name: " + name + ", "
					+ "Query: <a href=\"http://overpass-turbo.eu/?Q="
					+ UmlautConverter.makeProperURL(query)
					+ "&R\">View with Overpass turbo</a>");
		} else {
			log.error("Adding of relation failed!");
		}
		osm = null;
		return success;
	}

	/**
	 * Adds a new relation to the monitor.
	 * 
	 * @param query
	 *            The query to get the relation.
	 * @param name
	 *            The name of the query.
	 * @return {@code true} if the relation was successfully added.
	 */
	public boolean addNewRelationToMonitor(OverpassQuery query, String name) {
		boolean success = false;
		String path = FileDownloader.download(query.getQuery(), name);
		MyOSM osm = null;
		try {
			osm = Parser.parseOSMFile(path, name);
			osm.setQuery(query.getQuery());
			deleteFile(path);
			if (osm.getRelations().size() != 0) {
				success = saveOSM(osm, name);
			}
		} catch (Exception e) {
			log.error("File Parsing failed: " + e.getCause());
		}
		if (success) {
			rssAddMessage(name, "Name: " + name + ", "
					+ "Query: <a href=\"http://overpass-turbo.eu/?Q="
					+ UmlautConverter.makeProperURL(query.toString())
					+ "&R\">View with Overpass turbo</a>");
		} else {
			log.error("Adding of relation failed! Maybe the result set contained no relations.");
		}
		osm = null;
		return success;
	}

	/**
	 * Generates a new RSS Feed for the new relation. Also adds an RSS item to
	 * the OSMarelmon standard feed.
	 * 
	 * @param feedName
	 *            The name of the new feed.
	 * @param query
	 *            The query of the new feed.
	 */
	private void rssAddMessage(String feedName, String query) {
		// Standard OSMarelmon Feed
		RSSFeedManager.generateRSSEntry(
				util.Constants.OSMARELMON_STANDARD_FEED,
				"New Relation added to OSMarelmon: " + feedName,
				query.toString());

		// individual file feed
		String title = "Added new query to OSMarelmon: " + feedName;
		RSSFeedManager.generateRSSEntry(
				UmlautConverter.deleteUmlauts(feedName), title,
				query.toString());
		log.info("New relation added to OSMarelmon: " + feedName);
	}

	/**
	 * Triggers the checking of all monitored relations. Prints it to RSS feed.
	 */
	public void triggerCheckingForMonitoredRelations() {
		File dir = new File(util.Constants.HOME_DIR);
		MyOSM upd = null;
		MyOSM ori = null;
		String query = null;
		File[] fileList = dir.listFiles();
		if (fileList != null) {
			StringBuilder content = null;
			for (File f : fileList) {
				content = null;
				String fileName = f.getName();
				if (fileName.endsWith(util.Constants.FILE_ENDING)) {
					// load original
					ori = loadOSM(f.getAbsolutePath());
					query = ori.getQuery();
					// load new version from overpass remove .myosm from name
					fileName = fileName.substring(0, fileName.lastIndexOf('.'));
					try {
						String path = FileDownloader.download(query, fileName);
						upd = Parser.parseOSMFile(path, fileName);
						upd.setQuery(query);
						deleteFile(path);
					} catch (Exception e) {
						log.error("File Parsing failed");
					}
					log.info("Checking relation " + fileName + " with query: "
							+ query);
					// compare the two versions
					List<ChangedRelationContainer> con = RelationChecker
							.checkDocument(ori, upd);
					if (con == null) {
						String title = "Error at query: " + query;
						RSSFeedManager
								.generateRSSEntry(fileName, title,
										"An error occured. Documents cannot be compared.");
					} else if (!con.isEmpty()) {
						log.info("Changes in relation " + fileName);
						content = new StringBuilder();
						int changes = 0;
						for (ChangedRelationContainer c : con) {
							content.append(c.print());
							changes += c.getChangeSize();
						}
						String title = changes + " changes at query: "
								+ fileName;
						String description = content.toString();
						RSSFeedManager.generateRSSEntry(fileName, title,
								description);
						// save the new version of the query
						saveOSM(upd, fileName);
						ori = null;
						upd = null;
					}
				}
			}
		}
	}

	/**
	 * 
	 * @return a list of all monitored relations.
	 */
	public List<String> getMonitoredRelations() {
		List<String> names = new LinkedList<String>();
		File dir = new File(util.Constants.HOME_DIR);
		File[] fileList = dir.listFiles();
		if (fileList != null) {
			for (File f : fileList) {
				String fileName = f.getName();
				if (fileName.endsWith(".myosm")) {
					fileName = fileName.substring(0, fileName.lastIndexOf('.'));
					names.add(fileName);
				}
			}
		}
		return names;
	}

	/**
	 * Writes a MyOSM file to the HDD.
	 * 
	 * @param saveOSM
	 *            The OSM to save.
	 * @param name
	 *            The name of the osm file.
	 * @return True if the saving was successful.
	 */
	private synchronized boolean saveOSM(MyOSM saveOSM, String name) {
		boolean saved = false;
		String path = util.Constants.HOME_DIR + name;
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		if (!path.endsWith(util.Constants.FILE_ENDING)) {
			path = path + util.Constants.FILE_ENDING;
		}

		try {
			fos = new FileOutputStream(path);
			out = new ObjectOutputStream(fos);
			out.writeObject(saveOSM);
			out.close();
			saved = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saved;
	}

	/**
	 * Loads an stored MyOSM from the hdd. If the file was not found returns
	 * null.
	 * 
	 * @param path
	 *            The path to the MyOSM.
	 * @return The MyOSM object if everything is OK. Otherwise null.
	 */
	private synchronized MyOSM loadOSM(String path) {

		FileInputStream fis = null;
		ObjectInputStream in = null;
		MyOSM result = null;

		try {
			;
			fis = new FileInputStream(path);
			in = new ObjectInputStream(fis);
			result = (MyOSM) in.readObject();
			in.close();
		} catch (IOException e) {
			log.error("No original file found!");
		} catch (ClassNotFoundException e) {
			log.error("No original file found!");
		}

		return result;
	}

	/**
	 * Deletes the temporary download files in order to save hdd space.
	 * 
	 * @param path
	 *            The path to the file.
	 * @throws IOException
	 */
	public synchronized void deleteFile(String path) throws IOException {
		File file = new File(path);
		boolean success = true;
		log.info("Trying to delete file " + path);
		if (!file.exists()) {
			log.error("File can not be found!");
			success = false;
		}
		if (!file.delete()) {
			success = false;
			log.error("File can not be deleted!");
		}
		if (success) {
			log.info("Delete successful");
		}
	}
}
