package util;

import java.util.Map;

import types.ChangedNode;
import types.State;
import br.zuq.osm.parser.model.AbstractNode;
import br.zuq.osm.parser.model.OSMNode;

/**
 * Creates the changefile for a relation.
 * 
 * @author sebastian
 * 
 */
public class ChangeFileCreator {

	private ChangeFileCreator() {
	}

	/**
	 * Writes all changes between two nodes in a ChangedElement object. If one
	 * node is null that means a node has been created or deleted. It is illegal
	 * two call the method with null in both parameters. You will get null as
	 * return value
	 * 
	 * @param ori
	 *            The original node or null.
	 * @param upd
	 *            The updated node or null.
	 * @param state
	 * 
	 * @return The created ChangeObject or null.
	 */
	public static ChangedNode createChNode(OSMNode ori, OSMNode upd, State state) {
		ChangedNode chNode = null;
		if (ori == null && upd == null) {
			return null;
		} else if (ori == null) {

			// if it is a new node
			chNode = new ChangedNode(upd, state);
			chNode.setUpdLat(upd.lat);
			chNode.setUpdLon(upd.lon);
			// chNode.setVisible("null");
			// chNode.setUpdVisible(upd.visible);
			chNode.setUpdChangeset(upd.changeset);
			chNode.setUpdUser(upd.user + ", " + upd.uid);
			chNode.setTags(upd.tags.toString());
		} else if (upd == null) {

			// if the node was deleted
			chNode = new ChangedNode(ori, state);
			chNode.setLat(ori.lat);
			chNode.setLon(ori.lon);
			// chNode.setVisible(ori.visible);
			// chNode.setUpdVisible("null");
			chNode.setUpdChangeset("not available");
			chNode.setUser(ori.user + ", " + ori.uid);
			chNode.setTags(ori.tags.toString());
		} else {

			// node was edited
			chNode = new ChangedNode(upd, state);
			String lat = ori.lat;
			String lon = ori.lon;

			chNode.setLat(lat);
			chNode.setLon(lon);
			chNode.setVisible(ori.visible);
			chNode.setOriChangeset(ori.changeset);
			chNode.setUser(ori.user + ", " + ori.uid);

			if (!lat.equals(upd.lat) || !lon.equals(upd.lon)) {
				chNode.setUpdLat(upd.lat);
				chNode.setUpdLon(upd.lon);
			}
			/*
			 * if (!ori.visible.equals(upd.visible)) {
			 * chNode.setUpdVisible(upd.visible); } else {
			 * chNode.setUpdVisible("NOT EDITED"); }
			 */
			if (!ori.changeset.equals(upd.changeset)) {
				chNode.setUpdChangeset(upd.changeset);
			}
			if (!ori.uid.equals(upd.uid) || !ori.user.equals(upd.user)) {
				chNode.setUpdUser(upd.user + ", " + upd.uid);
			}
			chNode.setTags(getChangedTags(ori, upd));
		}
		return chNode;
	}

	/**
	 * Checks the tags of the OSM type for changes. Works for nodes, ways and
	 * relations.
	 * 
	 * @param ori
	 *            The original object.
	 * @param upd
	 *            The new object.
	 * @return The changes, if there were no changes the String is empty.
	 */
	public static String getChangedTags(AbstractNode ori, AbstractNode upd) {
		StringBuilder changedTags = new StringBuilder();
		for (Map.Entry<String, String> oriTag : ori.tags.entrySet()) {
			// tag still there
			if (upd.tags.containsKey(oriTag.getKey())) {
				String oriTV = oriTag.getValue();
				String updTV = upd.tags.get(oriTag.getKey());
				if (!oriTV.equals(updTV)) {
					// tag edited
					changedTags.append(oriTag.getKey() + ": " + oriTV + " -> "
							+ updTV + "\n");
				}
			} else {
				// tag deleted
				changedTags.append(oriTag.getKey() + ": " + State.DELETED
						+ "\n");
			}
		}
		for (Map.Entry<String, String> updTag : upd.tags.entrySet()) {
			// tag added
			if (!ori.tags.containsKey(updTag.getKey())) {
				changedTags.append(updTag.getKey() + ": " + State.CREATED
						+ ", Value: " + updTag.getValue() + "\n");
			}
		}
		return changedTags.toString();
	}
}
