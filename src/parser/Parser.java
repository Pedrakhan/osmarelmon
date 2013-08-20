package parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import types.MyOSM;
import br.zuq.osm.parser.OSMParser;
import br.zuq.osm.parser.model.OSM;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Relation;
import br.zuq.osm.parser.model.Way;

/**
 * Connector between my own code and the code of Willy Tiengo and zuq.
 * 
 * @author sebastian
 * 
 */
public class Parser {

	private static final Log log = LogFactory.getLog(Parser.class);

	private Parser() {
	}

	/**
	 * Parses an OSM file.
	 * 
	 * @param url
	 *            The URL to the .osm document. Returns null if the parsing
	 *            failed.
	 * @param name
	 * @return A whole OSM file splitted in nodes, ways and relations. Null if
	 *         parsing failed.
	 * @throws Exception
	 *             if there is a problem with parsing the file.
	 */
	public static synchronized MyOSM parseOSMFile(String url, String name)
			throws Exception {
		MyOSM osm = new MyOSM();
		long start = System.currentTimeMillis();
		OSM temp = OSMParser.parse(url);
		boolean success = true;

		// adding the osm types to my own osm data format
		osm.setName(name);
		for (OSMNode node : temp.getNodes()) {
			osm.addNode(node);
		}
		for (Way way : temp.getWays()) {
			osm.addWay(way);
		}
		for (Relation rel : temp.getRelations()) {
			osm.addRelation(rel);
		}
		long stop = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("Parsing time of " + url + ": " + (stop - start) + "ms");
		}
		temp = null;
		if (success) {
			return osm;
		} else {
			return null;
		}
	}
}
