package types;

import java.util.LinkedList;

import util.UmlautConverter;
import br.zuq.osm.parser.model.Member;
import br.zuq.osm.parser.model.Relation;

/**
 * Contains all elements of a relation which have changed and lists the changes.
 * 
 * @author sebastian
 * 
 */
public class ChangedRelationContainer implements ChangedType {
	private LinkedList<ChangedNode> nodes;
	private LinkedList<ChangedWay> ways;
	private LinkedList<ChangedMemberRelation> cMemRels;
	private String id;
	private String changedTags;
	private String changeset;
	private Relation rel;
	private boolean edited;
	private State state;

	public ChangedRelationContainer(Relation rel) {
		this.rel = rel;
		this.id = rel.id;
		this.changeset = rel.changeset;
		nodes = new LinkedList<ChangedNode>();
		ways = new LinkedList<ChangedWay>();
		cMemRels = new LinkedList<ChangedMemberRelation>();
		this.edited = false;
	}

	public void addNode(ChangedNode node) {
		this.edited = true;
		this.nodes.add(node);
	}

	public void addWay(ChangedWay way) {
		this.edited = true;
		this.ways.add(way);
	}

	public void addChangedTags(String tags) {
		// FIXME wieso war die if abfrage gut?
		if (!tags.isEmpty()) {
			this.edited = true;
			this.changedTags = tags;
		}
	}

	public void addMemberRelation(Member m, State state) {
		this.edited = true;
		this.cMemRels.add(new ChangedMemberRelation(m, state));
	}

	public void setState(State state) {
		this.state = state;
	}

	public boolean isEditedRelation() {
		return this.edited;
	}

	public String getChangeset() {
		return this.changeset;
	}

	public int getChangeSize() {
		return this.nodes.size() + this.ways.size() + this.cMemRels.size();
	}

	@Override
	public String print() {
		StringBuilder result = new StringBuilder("");
		result.append("<b><font color=\"darkred\">" + rel.toString()
				+ "</font></b> was " + this.state
				+ " <a href=\"http://www.openstreetmap.org/browse/relation/"
				+ this.id + "\">(browse)</a><br/>");
		for (ChangedNode node : nodes) {
			result.append(node.print());
		}
		for (ChangedWay way : ways) {
			result.append(way.print());
		}
		for (ChangedMemberRelation m : cMemRels) {
			result.append(m.print());
		}
		if (changedTags != null) {
			result.append("<br/>Edited Relation Tags: " + changedTags);
		}
		result.append("<br/><br/>");
		String resultWithoutUmlauts = UmlautConverter
				.convertUmlautsToHTML(result.toString());
		return resultWithoutUmlauts;
	}

	@Override
	public State getState() {
		// unused
		return null;
	}
}
