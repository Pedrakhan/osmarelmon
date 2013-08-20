/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.zuq.osm.parser.model;

import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * 
 * @author Willy Tiengo
 */
public class OSMNode extends AbstractNode {

	public String lat;
	public String lon;

	public OSMNode(String id, String visible, String timestamp, String version,
			String changeset, String user, String uid, String lat, String lon,
			Map<String, String> tags) {
		super(id, visible, timestamp, version, changeset, user, uid, tags);
		this.lat = lat;
		this.lon = lon;
		this.tags = tags;
	}

	public String getLocation() {
		Point p = new GeometryFactory().createPoint(new Coordinate(Double
				.valueOf(lon), Double.valueOf(lat)));

		return WKBWriter.bytesToHex(new WKBWriter().write(p));
	}

	// added by sebastian

	@Override
	public int hashCode() {
		long id = Long.parseLong(this.id);
		return (int) (id % Integer.MAX_VALUE);
	}

	/**
	 * Returns true if id and version are the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OSMNode) {
			return (this.id.equals(((OSMNode) obj).id));
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "(" + this.id + ", version: " + this.version + ")";
	}
}
