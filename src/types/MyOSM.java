package types;

import java.io.Serializable;
import java.util.HashMap;

import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Relation;
import br.zuq.osm.parser.model.Way;

/**
 * Represents an OSM document. Contains nodes, ways and relations.
 * 
 * @author sebastian
 * 
 */
public class MyOSM implements Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<Long, OSMNode> nodes;
	private HashMap<Long, Relation> relations;
	private HashMap<Long, Way> ways;
	private String id;
	private String query;
	private String name;

	/**
	 * Default constructor.
	 */
	public MyOSM() {
		nodes = new HashMap<Long, OSMNode>();
		relations = new HashMap<Long, Relation>();
		ways = new HashMap<Long, Way>();
		id = null;
		query = null;
		name = null;
	}

	/**
	 * Adds a node to the MyOSM object.
	 * 
	 * @param node
	 *            Has to be added.
	 * @return True if the node was successfully added.
	 */
	public void addNode(OSMNode node) {
		nodes.put(new Long(node.id), node);
	}

	/**
	 * Adds a way to the MyOSM object.
	 * 
	 * @param way
	 *            Has to be added.
	 * @return True if the way was successfully added.
	 */
	public void addWay(Way way) {
		ways.put(new Long(way.id), way);
	}

	/**
	 * Adds a relation to the MyOSM object.
	 * 
	 * @param rel
	 *            Has to be added.
	 * @return True if the relation was successfully added.
	 */
	public void addRelation(Relation rel) {
		if (rel.tags.containsValue("bus")) {
			// FIXME bei mehreren Busrelationen pro Datei sehr schlecht
			this.id = rel.id;
		}
		relations.put(new Long(rel.id), rel);
	}

	public String getID() {
		return this.id;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return this.query;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Prints all relations contained in the MyOSM object.
	 */
	public void printRelation() {
		System.out.println("Printing relations...");
		System.out.println(relations.toString());
	}

	public HashMap<Long, OSMNode> getNodes() {
		return nodes;
	}

	public HashMap<Long, Relation> getRelations() {
		return relations;
	}

	public HashMap<Long, Way> getWays() {
		return ways;
	}
}
