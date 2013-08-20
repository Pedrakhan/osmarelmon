package types;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import util.UmlautConverter;
import br.zuq.osm.parser.model.OSMNode;
import br.zuq.osm.parser.model.Way;

/**
 * Contains all informations of a way which has been updated.
 * 
 * @author sebastian
 * 
 */
public class ChangedWay implements ChangedType {
	HashMap<Long, ChangedNode> edNodes;
	List<OSMNode> unchangedNodes;
	private String id;
	private String name;
	private boolean changed;
	private String changedTags;
	private String userUID;
	private String changeset;
	private State state;

	/**
	 * Constructor can be used for added, deleted and edited ways. If it is an
	 * edited way be careful to deliver the new way and not the old one.
	 * 
	 * @param way
	 *            The way which has changed.
	 * @param state
	 *            The state of the way.
	 */
	public ChangedWay(Way way, State state) {
		if (state == State.EDITED) {
			this.id = way.id;
			this.name = way.getName();
			this.state = state;
			this.changed = false;
			this.unchangedNodes = way.nodes;
			this.changedTags = null;
			this.edNodes = new HashMap<Long, ChangedNode>();
			this.userUID = way.user + ", " + way.uid;
			this.changeset = way.changeset;
		} else {
			// FIXME falls er gelöscht wurde braucht man eher das Changeset in
			// dem er gelöscht wurde statt dem alten aus der relation
			this.id = way.id;
			this.name = way.getName();
			this.state = state;
			this.changedTags = way.tags.toString();
			this.edNodes = null;
			this.unchangedNodes = way.nodes;
			this.userUID = way.user + ", " + way.uid;
			// Es kann keine Aussage getroffen werden, in welchem Changeset der
			// Weg gelöscht wurde !
			if (state == State.CREATED) {
				this.changeset = way.changeset;
			}
		}
	}

	public void addChangedNode(ChangedNode ed) {
		this.changed = true;
		ed.setAnchor(true);
		edNodes.put(ed.getID(), ed);
	}

	public void addChangedTags(String tags) {
		// FIXME wieso war die if abfrage gut?
		if (!tags.isEmpty()) {
			this.changed = true;
			this.changedTags = tags;
		}
	}

	public void setChangeset(String changeset) {
		this.changed = true;
		this.changeset = changeset;
	}

	public void setUserUID(String userUID) {
		this.changed = true;
		this.userUID = userUID;
	}

	@Override
	public State getState() {
		return state;
	}

	public boolean hasChanged() {
		return this.changed;
	}

	/**
	 * 
	 * @return {@code true} if no node of the way was changed.
	 */
	public boolean containsChangedNodes() {
		return !edNodes.isEmpty();
	}

	@Override
	public String print() {
		StringBuilder result = new StringBuilder();
		result.append("<b><font color=\"darkorange\">way</font></b> id=\""
				+ this.id + ", name: " + this.name + "\" was \"" + this.state
				+ " <a href=\"http://www.openstreetmap.org/browse/way/"
				+ this.id + "\">(browse)</a>");
		result.append("<br/>    Last changes by <b>user</b>=\"" + this.userUID
				+ "\" in <b>changeset</b> "
				+ "<a href=\"http://www.openstreetmap.org/browse/changeset/"
				+ this.changeset + "\">" + this.changeset + "</a><br/>");
		if (edNodes != null) {
			result.append("<br/>    <b>edited nodes</b>:<br/>");
			for (Entry<Long, ChangedNode> e : edNodes.entrySet()) {
				ChangedNode n = e.getValue();
				result.append("<a href=\"http://www.openstreetmap.org/browse"
						+ "/node/" + n.getID() + "/history\">" + n.toString()
						+ "</a><br/>");
			}
		} else if (unchangedNodes != null) {
			result.append("<br/>    <b>nodes</b>=\""
					+ unchangedNodes.toString() + "\"");
		}
		if (changedTags != null) {
			if (this.state == State.EDITED) {
				result.append("<br/>    <b>Edited tags</b>");
			} else {
				result.append("<br/>    <b>Tags</b>");
			}
			result.append("=\"" + changedTags + "\"");
		}
		result.append("<br/><br/>");
		String resultWithoutUmlauts = UmlautConverter
				.convertUmlautsToHTML(result.toString());
		return resultWithoutUmlauts;
	}
}
