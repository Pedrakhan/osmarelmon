package types;

/**
 * Represents an Overpass Query. Offers methods to generate queries.
 * 
 * @author sebastian
 * 
 */
public class OverpassQuery {
	private static final int KEY_VALUE_AMOUNT = 2;
	private String query;
	private String name;

	/**
	 * Constructor to generate a query.
	 * 
	 * @param tags
	 *            The key-value pairs.
	 */
	public OverpassQuery(String... tags) {
		generateQuery(tags);
	}

	/**
	 * Generates an Overpass Query out of the given tags.
	 * 
	 * @param tags
	 *            The key value pairs.
	 */
	private void generateQuery(String... tags) {
		StringBuilder query = new StringBuilder();
		if (tags.length % KEY_VALUE_AMOUNT == 0 && tags.length > 0) {
			query.append("(" + "relation");
			for (int i = 0; i < tags.length; i += KEY_VALUE_AMOUNT) {
				query.append("[\"" + tags[i] + "\"=\"" + tags[i + 1] + "\"]");
			}
			query.append(";");
			query.append("node(r)->.nodes;way(r);" + "node(w);" + ");"
					+ "out meta;");
		}
		this.query = query.toString();
	}

	/**
	 * Sets the name of the query.
	 * 
	 * @param name
	 *            The query name.
	 */
	public void setQueryName(String name) {
		this.name = name;
	}

	/**
	 * Returns only the overpass query.
	 * 
	 * @return the overpass query.
	 */
	public String getQuery() {
		return query;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + ", Query: " + this.query;
	}
}
