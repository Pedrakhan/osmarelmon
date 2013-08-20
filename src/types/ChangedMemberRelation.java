package types;

import br.zuq.osm.parser.model.Member;

/**
 * Contains all informations of a member relation, i.e. a relation in another
 * relation.
 * 
 * @author sebastian
 * 
 */
public class ChangedMemberRelation implements ChangedType {

	private Member m;
	private State state;

	public ChangedMemberRelation(Member m, State state) {
		this.m = m;
		this.state = state;
	}

	@Override
	public String print() {
		return "Changed member relation: " + m.ref + ": " + this.state
				+ "<br/>";
	}

	@Override
	public State getState() {
		return this.state;
	}
}
