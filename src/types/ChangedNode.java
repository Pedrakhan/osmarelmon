package types;

import util.UmlautConverter;
import br.zuq.osm.parser.model.OSMNode;

/**
 * Bean for a OSMNode plus changes.
 * 
 * @author sebastian
 * 
 */
public class ChangedNode implements ChangedType {

	private String id;
	private boolean anchor;
	private State state;
	private String oriLat;
	private String oriLon;
	private String oriVisible;
	private String oriChangeset;
	private String oriUser;
	private String oriTags;
	private String updLat;
	private String updLon;
	private String updVisible;
	private String updChangeset;
	private String updUser;

	public ChangedNode(OSMNode node, State state) {
		this.id = node.id;
		this.anchor = false;
		this.state = state;
		oriLat = null;
		oriLon = null;
		oriVisible = null;
		oriChangeset = null;
		oriUser = null;
		oriTags = null;
	}

	public void setUpdLat(String updLat) {
		this.updLat = updLat;
	}

	public void setAnchor(boolean anchor) {
		this.anchor = anchor;
	}

	public void setUpdLon(String updLon) {
		this.updLon = updLon;
	}

	public void setUpdVisible(String updVisible) {
		this.updVisible = updVisible;
	}

	public void setUpdChangeset(String updChangeset) {
		this.updChangeset = updChangeset;
	}

	public void setUpdUser(String updUser) {
		this.updUser = updUser;
	}

	public void setLat(String lat) {
		this.oriLat = lat;
	}

	public void setLon(String lon) {
		this.oriLon = lon;
	}

	public void setVisible(String visible) {
		this.oriVisible = visible;
	}

	public void setOriChangeset(String changeset) {
		this.oriChangeset = changeset;
	}

	public void setUser(String user) {
		this.oriUser = user;
	}

	public void setTags(String tags) {
		this.oriTags = tags;
	}

	public Long getID() {
		return Long.parseLong(this.id);
	}

	@Override
	public State getState() {
		return this.state;
	}

	@Override
	public String toString() {
		return "Node: " + this.id + ", " + this.state;
	}

	@Override
	public String print() {
		StringBuilder result = new StringBuilder("");
		// if (anchor) {
		// result.append("<a href=\"id.htm#" + this.id + "\"></a>");
		// }
		result.append("<b><font color=\"blue\">node</font></b>  id=\""
				+ this.id + "\" state=\"" + this.state
				+ "\"<a href=\"http://www.openstreetmap.org/browse/node/"
				+ this.id + "/history\">(browse)</a>");

		switch (this.state) {
		case EDITED:
			// geographische Positionsänderung
			if (this.oriLat != null && this.oriLon != null
					&& this.state == State.EDITED) {
				double lat1 = Double.parseDouble(oriLat) * Math.PI / 180;
				double lon1 = Double.parseDouble(oriLon) * Math.PI / 180;
				if (updLat != null && updLon != null) {
					double lat2 = Double.parseDouble(updLat) * Math.PI / 180;
					double lon2 = Double.parseDouble(updLon) * Math.PI / 180;
					double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2)
							+ Math.cos(lat1) * Math.cos(lat2)
							* Math.cos(lon1 - lon2)) * 6378.388 * 1000;
					result.append("<br/>    Node has been moved. <b>Distance</b>: "
							+ distance + " m");
				}
			}
			result.append("<br/>    Last changes by <b>user</b>=\""
					+ this.updUser
					+ "\" in <b>changeset</b> "
					+ "<a href=\"http://www.openstreetmap.org/browse/changeset/"
					+ this.updChangeset + "\">" + this.updChangeset
					+ "</a><br/>");
			break;
		case DELETED:
			result.append("<br/>    Node has been deleted in <b>changeset</b> "
					+ this.updChangeset
					+ "<a href=\"http://www.openstreetmap.org/browse/changeset/"
					+ this.updChangeset + "\">" + this.updChangeset
					+ "</a><br/>");
			break;
		case CREATED:
			result.append("<br/>    Node has been created by <b>user</b> "
					+ this.updUser
					+ " in <b>changeset</b> "
					+ this.updChangeset
					+ "<a href=\"http://www.openstreetmap.org/browse/changeset/"
					+ this.updChangeset + "\">" + this.updChangeset
					+ "</a><br/>");
			break;
		default:
			break;
		}
		if (this.oriTags != null && !this.oriTags.equals("{}")
				&& !this.oriTags.isEmpty()) {
			result.append("Tags of node = \"" + this.oriTags + "\"");
		}
		result.append("<br/><br/>");
		String resultWithoutUmlauts = UmlautConverter
				.convertUmlautsToHTML(result.toString());
		return resultWithoutUmlauts;
	}
}
