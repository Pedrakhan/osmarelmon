package util;

import java.util.List;

/**
 * The query builder generates Overpass queries out of Strings.
 * 
 * @author sebastian
 * 
 */
public class QueryBuilder {

	private static final int KEY_VALUE_AMOUNT = 2;

	private QueryBuilder() {
	}

	/**
	 * Generates a query to get a relation with the specified ID with all nodes
	 * and all ways.
	 * 
	 * @param ID
	 *            The ID of the relation.
	 * @return The query as string.
	 */
	public static String getIDQuery(String ID) {
		String result = "(" + "relation(" + ID + ");" + "node(r)->.nodes;"
				+ "way(r);" + "node(w);" + ");" + "out meta;";
		return result;
	}

	/**
	 * Generates a query for a relation which is specified by its tags.
	 * Therefore the key value pairs have to be entered in this order: key value
	 * key value .... If the amount of the key value strings is not even the
	 * method returns null.
	 * 
	 * @param strings
	 *            The key value pairs: <key> <value>
	 * @return The generated query or null if the input was illegal.
	 */
	public static String getRelationQuery(List<String> strings) {
		StringBuilder query = new StringBuilder();
		if (strings.size() % KEY_VALUE_AMOUNT == 0 && strings.size() > 0) {
			query.append("(" + "relation");
			for (int i = 0; i < strings.size(); i += KEY_VALUE_AMOUNT) {
				query.append("[\"" + strings.get(i) + "\"=\""
						+ strings.get(i + 1) + "\"]");
			}
			query.append(";");
			query.append("node(r)->.nodes;way(r);" + "node(w);" + ");"
					+ "out meta;");
		}
		return query.toString();
	}
}
